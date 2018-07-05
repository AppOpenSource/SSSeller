package com.abt.seller.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.imgload.ImgLoad;
import com.abt.seller.listtest.MusicListView;
import com.abt.seller.models.Order;
import com.abt.seller.services.OrdersService;
import com.abt.seller.utils.DateUtil;
import com.abt.seller.utils.ToastUtil;
import com.abt.seller.views.PullRefreshView;
import com.abt.seller.views.PullRefreshView.OnFooterRefreshListener;
import com.abt.seller.views.PullRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends Activity implements OnClickListener,
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private static final int CUR = 0;
	private static final int HIS = 1;
	private static final int NORMAL = 2;
	private static final int HEAD = 3;
	private static final int FOOT = 4;
	private TextView curOrders, hisOrders;
	private ListView curListView, hisListView;
	private PullRefreshView curPullRefreshView, hisPullRefreshView;
	private OrdersAdapter curAdapter, hisAdapter;
	private ViewPager vPager;
	private Dialog proDialog;
	private List<Order> curOrdersList = new ArrayList<Order>();
	private List<Order> hisOrdersList = new ArrayList<Order>();
	private ArrayList<View> listViews = new ArrayList<View>();;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NORMAL:
				notifyDataChanged(msg.arg1);
			case HEAD:
				notifyDataChanged(msg.arg1);
				clearFresh(msg.arg1, HEAD);
				break;
			case FOOT:
				notifyDataChanged(msg.arg1);
				clearFresh(msg.arg1, FOOT);
				break;
			}
		};
	};
	
	private void notifyDataChanged(int type) {
		if (type == -1) showError();
		curAdapter.notifyDataSetChanged();
		hisAdapter.notifyDataSetChanged();
		proDialog.dismiss();
	}
	
	private void showError() {
		ToastUtil.show(this, "没有要显示的数据");
	}
	
	private void clearFresh(int tag, int position) {
		if (position == HEAD) {
			if (tag == CUR) {
				curPullRefreshView.onHeaderRefreshComplete();
			} else if (tag == HIS) {
				hisPullRefreshView.onHeaderRefreshComplete();
			}
		} else if (position == FOOT) {
			if (tag == CUR) {
				curPullRefreshView.onFooterRefreshComplete();
			} else if (tag == HIS) {
				hisPullRefreshView.onFooterRefreshComplete();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		initViews(getLayoutInflater());
	}

	private void initViews(LayoutInflater inflater) {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
		
		curOrders = (TextView) findViewById(R.id.current_orders);
		hisOrders = (TextView) findViewById(R.id.history_orders);
		curOrders.setOnClickListener(this);
		hisOrders.setOnClickListener(this);

		listViews.add(inflater.inflate(R.layout.current_listview, null));
		listViews.add(inflater.inflate(R.layout.history_listview, null));
		vPager = (ViewPager) findViewById(R.id.vPager);
		vPager.setAdapter(new VPagerAdapter(listViews));
		vPager.setCurrentItem(0);
		vPager.setOnPageChangeListener(new VPageChangeListener());

		curAdapter = new OrdersAdapter(this, R.layout.order_item, curOrdersList);
		curListView = (ListView) listViews.get(0).findViewById(R.id.current_listview);
		curListView.setAdapter(curAdapter);
		curListView.setDividerHeight(0);
		curListView.setCacheColorHint(0);
		curListView.setOnItemClickListener(curItemClickListener);

		hisAdapter = new OrdersAdapter(this, R.layout.order_item, hisOrdersList);
		hisListView = (ListView) listViews.get(1).findViewById(R.id.history_listview);
		hisListView.setAdapter(hisAdapter);
		hisListView.setDividerHeight(0);
		hisListView.setCacheColorHint(0);
		hisListView.setOnItemClickListener(hisItemClickListener);

		curPullRefreshView = (PullRefreshView) listViews.get(0).findViewById(R.id.cur_pull_refresh_view);
		curPullRefreshView.setOnHeaderRefreshListener(this);
		curPullRefreshView.setOnFooterRefreshListener(this);

		hisPullRefreshView = (PullRefreshView) listViews.get(1).findViewById(R.id.his_pull_refresh_view);
		hisPullRefreshView.setOnHeaderRefreshListener(this);
		hisPullRefreshView.setOnFooterRefreshListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getOrdersDatas(NORMAL, CUR);
	}
	
	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.cur_pull_refresh_view) {
			getOrdersDatas(HEAD, CUR);
		} else if (view.getId() == R.id.his_pull_refresh_view) {
			getOrdersDatas(HEAD, HIS);
		}
	}

	@Override
	public void onFooterRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.cur_pull_refresh_view) {
			getOrdersDatas(FOOT, CUR);
		} else if (view.getId() == R.id.his_pull_refresh_view) {
			getOrdersDatas(FOOT, HIS);
		}
	}
	
	private void getOrdersDatas(final int type, final int time) {
		final OrdersService os = new OrdersService(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				getOrders(os, type, time);
			}
		}).start();
	}

	private void getOrders(OrdersService os, int type, int time) {
		Message msg = handler.obtainMessage(type);
		msg.arg1 = time;
		List<Order> curOrder = os.getOrder(CUR);
		if (curOrder != null && !curOrder.isEmpty()) {
			if (curOrdersList != null)
				curOrdersList.clear();
				curOrdersList.addAll(curOrder);
		} else {
			msg.arg1 = -1;
		}
		List<Order> hisOrder = os.getOrder(HIS);
		if (hisOrder != null && !hisOrder.isEmpty()) {
			if (hisOrdersList != null)
				hisOrdersList.clear();
				hisOrdersList.addAll(hisOrder);
		}
		msg.sendToTarget();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.current_orders:
			curOrders.setBackgroundResource(R.drawable.select_bg_1_nor);
			hisOrders.setBackgroundResource(R.drawable.select_bg_1_pre);
			vPager.setCurrentItem(0);
			break;
		case R.id.history_orders:
			curOrders.setBackgroundResource(R.drawable.select_bg_1_pre);
			hisOrders.setBackgroundResource(R.drawable.select_bg_1_nor);
			vPager.setCurrentItem(1);
			break;
		}
	}

	private class VPagerAdapter extends PagerAdapter {
		private List<View> listviews;
		public VPagerAdapter(List<View> listviews) {
			this.listviews = listviews;
		}
		
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(listviews.get(arg1), 0);
			return listviews.get(arg1);
		}
		
		@Override
		public int getCount() {
			return listviews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private class VPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) { }
		@Override
		public void onPageScrollStateChanged(int arg0) { }
		
		@Override
		public void onPageSelected(int arg0) {
			setCurrentTab(arg0);
		}
	}
	
	private void setCurrentTab(int index) {
		switch (index) {
		case 0:
			curOrders.setBackgroundResource(R.drawable.select_bg_1_nor);
			hisOrders.setBackgroundResource(R.drawable.select_bg_1_pre);
			break;
		case 1:
			curOrders.setBackgroundResource(R.drawable.select_bg_1_pre);
			hisOrders.setBackgroundResource(R.drawable.select_bg_1_nor);
			break;
		}
	}

	private class OrdersAdapter extends ArrayAdapter<Order> {
		public OrdersAdapter(Context cx, int resId, List<Order> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.order_item, null);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position));
			return cv;
		}
	}
	
	private void findViews(ViewHolder holder, View cv) {
		holder.orderNo = (TextView) cv.findViewById(R.id.order_no);
		holder.consiginee = (TextView) cv.findViewById(R.id.order_consiginee);
		holder.phone = (TextView) cv.findViewById(R.id.order_phone);
		holder.location = (TextView) cv.findViewById(R.id.order_location);
		holder.price = (TextView) cv.findViewById(R.id.order_price);
		holder.time = (TextView) cv.findViewById(R.id.order_time);
		holder.status = (TextView) cv.findViewById(R.id.order_status);
		holder.orderPic = (ImageView) cv.findViewById(R.id.order_pic);
		holder.mlv = (MusicListView) cv.findViewById(R.id.music_list_view);
	} 
	
	private void refreshItem(ViewHolder holder, Order order) {
		Log.d("TAG", "order.getStatus() : " + order.getStatus());
		switch (order.getStatus()) {
		case 1:
			holder.status.setText("未确认");
			break;
		case 2:
			holder.status.setText("已撤销");
		case 3:
			holder.status.setText("备货中");
			break;
		case 4:
			holder.status.setText("备货完成，准备派送");
			break;
		case 5:
			holder.status.setText("派送中");
			break;
		case 6:
			holder.status.setText("已完成");
			break;
		}
		holder.orderNo.setText(order.getOrdernum() + "");
		holder.consiginee.setText(order.getLinkman());
		holder.phone.setText(order.getPhone());
		holder.location.setText(order.getAddress());
		holder.price.setText(order.getTotal() + "");
		holder.time.setText(DateUtil.getFormatedDate(order.getAddtime()));
		String imgPath = order.getOrderGood().get(0).getPicpath();
		ImgLoad loader = ImgLoad.getInstance();
		if (imgPath != null) {
			holder.orderPic.setTag(imgPath);
			loader.addTask(imgPath, holder.orderPic);
			loader.doTask();
		}
	}
	
	private class ViewHolder {
		TextView orderNo;
		TextView consiginee;
		TextView phone;
		TextView location;
		TextView price;
		TextView time;
		TextView status;
		ImageView orderPic;
		MusicListView mlv;
	}
	
	private OnItemClickListener curItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			jumpToDetails(curOrdersList.get(arg2));
		}
	};

	private OnItemClickListener hisItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			jumpToDetails(hisOrdersList.get(arg2));
		}
	};
	
	private void jumpToDetails(Order or) {
		Intent it = new Intent(this, OrderDetailsActivity.class);
		it.putExtra("order_id", or.getId());
		startActivity(it);
	}
}