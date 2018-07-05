package com.abt.seller.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.GoodOutline;
import com.abt.seller.models.OrderDetail;
import com.abt.seller.models.OrderTrackDetail;
import com.abt.seller.services.OrderConfirmService;
import com.abt.seller.services.OrderDetailService;
import com.abt.seller.services.OrderDetailTrackService;
import com.abt.seller.utils.DateUtil;
import com.abt.seller.utils.ListViewUtil;
import com.abt.seller.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends Activity {
	private static final int REFRESH_UI = 0;
	private static final int ORDER_CONFIRM = 1;
	private static final int REFRESH_TRACK = 2;
	private static final int ORDER_CANCEL = 3;
	private int orderid;
	private int shopsId = 0;
	private int status = 0;
	private int time = 0;
	private int preTime = 0;
	private int arrTime = 0;
	private TextView orderTotalPrice;
	private TextView orderDetailDate;
	private TextView orderNum;
	private TextView orderConsiginee;
	private TextView orderPhone;
	private TextView orderAddress;
	private TextView orderStatus;
	private LinearLayout timeBar;
	private EditText minute;
	private Button productSave;
	private Button productDel;
	private Dialog proDialog;
	private ListView listView;
	private ListView listView2;
	private TextView orderTimeTV;
	private OutlinesAdapter orderListAdapter;
	private TracksAdapter trackListAdapter;
	private List<GoodOutline> details = new ArrayList<GoodOutline>();
	private List<OrderTrackDetail> tracks = new ArrayList<OrderTrackDetail>();
	private OrderDetail orderDetail;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_UI:
				refreshUI((OrderDetail) msg.obj);
				break;
			case ORDER_CONFIRM:
				orderConfirm(msg);
				break;
			case ORDER_CANCEL:
				refreshBtn(msg.arg1);
				if (proDialog.isShowing()) proDialog.dismiss();
				break;
			case REFRESH_TRACK:
				refreshTrackList((List<OrderTrackDetail>)msg.obj);
				break;
			}
		}
	};

	private void refreshUI(OrderDetail detail) {
		refreshList(detail.getDetail());
		refreshPrice();
		refreshBtn(detail.getStatus());
		Log.d("TAG", "detail : " + detail.getStatus());
		Log.d("TAG", "detail getLinkman : " + detail.getLinkman());
		orderNum.setText(detail.getOrdernum());
		orderDetailDate.setText(DateUtil.getFormatedDate_1(detail.getAddtime()));
		orderConsiginee.setText(detail.getLinkman());
		orderPhone.setText(detail.getPhone());
		orderAddress.setText(detail.getAddress());
		if (proDialog.isShowing()) proDialog.dismiss();
	}
	
	private void refreshList(List<GoodOutline> ds) {
		if (ds != null && ds.size() > 0) {
			details.addAll(ds);
			orderListAdapter.notifyDataSetChanged();
			ListViewUtil.setListViewHeightBaseOnChildren(listView);
		}
	}
	
	private void refreshTrackList(List<OrderTrackDetail> ds) {
		if (ds != null && ds.size() > 0) {
			tracks.clear();
			tracks.addAll(ds);
			trackListAdapter.notifyDataSetChanged();
			ListViewUtil.setListViewHeightBaseOnChildren(listView2);
		}
		if (proDialog.isShowing()) proDialog.dismiss();
	}
	
	private void refreshPrice() {
		float price = 0;
		for (GoodOutline gd : details) {
			price += gd.getPrice() * gd.getNum();
		}
		orderTotalPrice.setText(price + "");		
	}

	private void refreshBtn(int status) {
		switch (status) {
		case 1:
			orderStatus.setText("未确认");
			productSave.setText("开始备货");
			timeBar.setVisibility(View.VISIBLE);
			orderTimeTV.setText("预计备货时间");
			break;
		case 2:
			orderStatus.setText("已撤销");
			productDel.setEnabled(false);
			productSave.setEnabled(false);
			productDel.setText("订单已撤销");
			productSave.setVisibility(View.GONE);
			productDel.setVisibility(View.GONE);
			timeBar.setVisibility(View.GONE);
			break;
		case 3:
			orderStatus.setText("备货中");
			productSave.setText("备货完成");
			timeBar.setVisibility(View.GONE);
			break;
		case 4:
			orderStatus.setText("备货完成，准备派送");
			productSave.setText("开始派送");
			timeBar.setVisibility(View.VISIBLE);
			orderTimeTV.setText("预计送达时间");
			break;
		case 5:
			orderStatus.setText("派送中");
			productSave.setText("完成");
			timeBar.setVisibility(View.GONE);
			break;
		case 6:
			orderStatus.setText("已完成");
			productSave.setVisibility(View.GONE);
			productDel.setVisibility(View.GONE);
			timeBar.setVisibility(View.GONE);
			productSave.setEnabled(false);
			productDel.setEnabled(false);
			break;
		case -1:
			ToastUtil.show(this, "订单状态修改失败");
			break;
//		case 1:
//			productDel.setText("未确认");
//		case 2:
//			productDel.setText("已撤销");
//		case 3:
//			productDel.setText("备货中");
//		case 4:
//			productDel.setText("备货完成，准备派送");
//		case 5:
//			productDel.setText("派送中");
//		case 6:
//			productDel.setText("已完成");
		}
	}

	private void orderConfirm(Message msg) {
		refreshBtn(msg.arg1);
		if (proDialog.isShowing()) proDialog.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		initViews();
		initDatas();
	}

	private void initViews() {
		orderNum = (TextView) findViewById(R.id.order_detail_no);
		orderDetailDate = (TextView) findViewById(R.id.order_detail_date);
		orderTotalPrice = (TextView) findViewById(R.id.order_detail_total);
		productSave = (Button) findViewById(R.id.product_save);
		productDel = (Button) findViewById(R.id.product_del);
		minute = (EditText) findViewById(R.id.minute);
		minute.setText("10");

		orderListAdapter = new OutlinesAdapter(this, R.layout.order_table_item, details);
		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(orderListAdapter);
		listView.setDividerHeight(0);
		listView.setCacheColorHint(0);
		
		trackListAdapter = new TracksAdapter(this, R.layout.order_track_item, tracks);
		listView2 = (ListView) findViewById(R.id.list_view_2);
		listView2.setAdapter(trackListAdapter);
		listView2.setDividerHeight(0);
		listView2.setCacheColorHint(0);
		
		orderTimeTV = (TextView) findViewById(R.id.orderTime);
		orderConsiginee = (TextView) findViewById(R.id.order_consiginee);
		orderPhone = (TextView) findViewById(R.id.order_phone);
		orderAddress = (TextView) findViewById(R.id.order_location);
		
		orderStatus = (TextView) findViewById(R.id.order_status);
		timeBar = (LinearLayout) findViewById(R.id.time_bar);
	}

	private void initDatas() {
		Intent in = getIntent();
		orderid = in.getIntExtra("order_id", -1);
		if (in != null && orderid != -1) {
			showDialog();
			getDetail();
		}
	}
	
	private void showDialog() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
	}
	
	private void getDetail() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getOrderDetail();
				getOrderTrackDetail();
			}
		}).start();		
	}
	
	private void getOrderDetail() {
		Log.d("TAG", "getOrderDetail()");
		OrderDetailService ods = new OrderDetailService(this);
		Message msg = handler.obtainMessage(REFRESH_UI);
		msg.obj = ods.getOrderDetail(orderid);
		orderDetail = (OrderDetail)msg.obj;
		msg.sendToTarget();
	}
	
	private void getOrderTrackDetail() {
		Log.d("TAG", "getOrderTrackDetail()");
		OrderDetailTrackService odts = new OrderDetailTrackService(this);
		Message msg = handler.obtainMessage(REFRESH_TRACK);
		msg.obj = odts.getOrderDetail(orderid);
		msg.sendToTarget();
	}
	
	public void back_onClick(View view) {
		finish();
	}
	
	public void add_onClick(View view) {
		int mi = Integer.parseInt(minute.getText().toString());
		time = mi + 5;
		timeSwitch(status);
		minute.setText(time + "");
	}

	public void dec_onClick(View view) {
		int mi = Integer.parseInt(minute.getText().toString());
		time = mi - 5;
		timeSwitch(status);
		minute.setText(time + "");
	}
	
	public void timeSwitch(int status) {
		switch (status) {
		case 3:
			preTime = time;
			break;
		case 5:
			arrTime = time;
			break;
		}
	}
	
	public void save_onClick(View view) {
		shopsId = orderDetail.getShopsid();
		switch (orderDetail.getStatus()) {
		case 1:
			status = 3;
			orderDetail.setStatus(status);
			preTime = Integer.parseInt(minute.getText().toString());
			break;
		case 2:
			orderDetail.setStatus(status);
			break;
		case 3:
			status = 4;
			orderDetail.setStatus(status);
			break;
		case 4:
			arrTime = Integer.parseInt(minute.getText().toString());
			status = 5;
			orderDetail.setStatus(status);
			break;
		case 5:
			status = 6;
			orderDetail.setStatus(status);
			break;
		}
		if (orderid != -1) confirmOrderAsync(ORDER_CONFIRM);
		
	}
	
	public void cancel_onClick(View view) {
		cancelOrder();
	}
	
	private void cancelOrder() {
		status = 2;
		shopsId = orderDetail.getShopsid();
		confirmOrderAsync(ORDER_CANCEL);
	}
	
	private void confirmOrderAsync(final int type) {
		showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				confirmOrder(type);
				getOrderTrackDetail();
			}
		}).start();
	}
	
	private void confirmOrder(int type) {
		OrderConfirmService ocs = new OrderConfirmService(this);
		Message msg = handler.obtainMessage(type);
		msg.arg1 = ocs.getOrderConfirm(orderid, shopsId, status, preTime, arrTime);
		msg.sendToTarget();
	}

	private class OutlinesAdapter extends ArrayAdapter<GoodOutline> {
		public OutlinesAdapter(Context cx, int resId, List<GoodOutline> obj) {
			super(cx, resId, obj);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.order_table_item, null);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, position, getItem(position));
			return cv;
		}
	}
	
	private void refreshItem(ViewHolder holder, int ps, GoodOutline line) {
		if (ps % 2 == 0) {
			holder.orderNo.setBackgroundColor(color(R.color.gray_back));
			holder.orderProduct.setBackgroundColor(color(R.color.gray_back));
			holder.orderCount.setBackgroundColor(color(R.color.gray_back));
			holder.orderPrice.setBackgroundColor(color(R.color.gray_back));
		}
		holder.orderNo.setText((ps + 1) + "");
		holder.orderProduct.setText(line.getName());
		holder.orderCount.setText(line.getNum() + "");
		holder.orderPrice.setText(line.getPrice() + "");
	}
	
	private int color(int c) {
		return getResources().getColor(c);
	}
	
	private void findViews(ViewHolder holder, View cv) {
		holder.orderNo = (TextView) cv.findViewById(R.id.order_table_no);
		holder.orderProduct = (TextView) cv.findViewById(R.id.order_table_content);
		holder.orderCount = (TextView) cv.findViewById(R.id.order_table_count);
		holder.orderPrice = (TextView) cv.findViewById(R.id.order_table_people);
	}
	
	private class ViewHolder {
		TextView orderNo;
		TextView orderProduct;
		TextView orderCount;
		TextView orderPrice;
	}
	
	private class TracksAdapter extends ArrayAdapter<OrderTrackDetail> {
		public TracksAdapter(Context cx, int resId, List<OrderTrackDetail> obj) {
			super(cx, resId, obj);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder2 holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.order_track_item, null);
				holder = new ViewHolder2();
				findViews2(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder2) cv.getTag();
			}
			refreshTrackItem(holder, position, getItem(position));
			return cv;
		}
	}
	
	private void refreshTrackItem(ViewHolder2 holder, int ps, OrderTrackDetail line) {
		if (ps % 2 == 0) {
			holder.orderTime.setBackgroundColor(color2(R.color.gray_back));
			holder.orderContent.setBackgroundColor(color2(R.color.gray_back));
//			holder.orderCount.setBackgroundColor(color2(R.color.gray_back));
			holder.orderPeople.setBackgroundColor(color2(R.color.gray_back));
		}
		holder.orderTime.setText(DateUtil.getFormatedDate(line.getAddtime()));
		holder.orderContent.setText(line.getContent());
		holder.orderPeople.setText(line.getPeople());
	}
	
	private int color2(int c) {
		return getResources().getColor(c);
	}
	
	private void findViews2(ViewHolder2 holder, View cv) {
		holder.orderTime = (TextView) cv.findViewById(R.id.order_table_time);
		holder.orderContent = (TextView) cv.findViewById(R.id.order_table_content);
		holder.orderPeople = (TextView) cv.findViewById(R.id.order_table_people);
	}
	
	private class ViewHolder2 {
		TextView orderTime;
		TextView orderContent;
		TextView orderPeople;
	}
}