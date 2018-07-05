package com.abt.seller.activitys;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.OrderMsgItem;
import com.abt.seller.services.AftMsgService;
import com.abt.seller.services.BefMsgService;
import com.abt.seller.services.CurMsgService;
import com.abt.seller.utils.DateUtil;
import com.abt.seller.utils.NotificationUtil;
import com.abt.seller.utils.ToastUtil;
import com.abt.seller.utils.ViewUtils;
import com.abt.seller.views.MsgTextView;
import com.abt.seller.views.PullRefreshView;
import com.abt.seller.views.PullRefreshView.OnFooterRefreshListener;
import com.abt.seller.views.PullRefreshView.OnHeaderRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MsgActivity extends ListActivity implements OnHeaderRefreshListener, 
		OnFooterRefreshListener {
	private static final int HEADER = 0;
	private static final int NEWEST = 1;
	private static final int FOOTER = 2;
	private PullRefreshView pullRefreshView;
	private MsgAdapter msgAdapter;
	private Dialog proDialog;
	private List<OrderMsgItem> msgData = new ArrayList<OrderMsgItem>();
	
	private Handler handler = new Handler() {
		@Suppreabtarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			List<OrderMsgItem> list = (List<OrderMsgItem>)msg.obj;
			switch (msg.what) {
			case NEWEST:
				refreshNewest(list);
				break;
			case HEADER:
				refreshHeader(list);
				break;
			case FOOTER:
				refreshFooter(list);
				break;
			}
		}
	};
	
	private void refreshNewest(List<OrderMsgItem> newest) {
		msgAdapter = new MsgAdapter(this, R.layout.msg_list, msgData);
		setListAdapter(msgAdapter);
		if (newest != null && !newest.isEmpty()) {
			msgData.addAll(newest);
			msgAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.show(this, "没有要显示的数据");
		}
		proDialog.dismiss();
	}
	
	private void refreshHeader(List<OrderMsgItem> head) {
		if (head != null && !head.isEmpty()) {
			msgData.addAll(head);
			msgAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.show(this, R.string.no_new_msg_any_more);
		}
		pullRefreshView.onHeaderRefreshComplete();
	}
	
	private void refreshFooter(List<OrderMsgItem> foot) {
		if (foot != null && !foot.isEmpty()) {
			msgData.addAll(foot);
			msgAdapter.notifyDataSetChanged();
			TabsActivity.badgeView.setText(foot.size() + "");
		} else {
			TabsActivity.badgeView.hide();
			ToastUtil.show(this, R.string.no_msg_any_more);
		}
		pullRefreshView.onFooterRefreshComplete();
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		initViews();
		initDatas();
		NotificationUtil.cancelAll(this);
	}

	private void initViews() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();
		pullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh_view);
		pullRefreshView.setOnHeaderRefreshListener(this);
		pullRefreshView.setOnFooterRefreshListener(this);
		
		getListView().setOnItemClickListener(itemClickListener);
		if (!TabsActivity.badgeView.equals(null)) {
			TabsActivity.badgeView.hide();
		}
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			jumpToDetails(msgData.get(arg2));
		}
	};
	
	private void jumpToDetails(OrderMsgItem msg) {
		Intent in = new Intent(this, OrderDetailsActivity.class);
//		in.putExtra("order_id", msg.getOrderId());
		in.putExtra("order_id", msg.getOrderid());
		startActivity(in);		
	}
	
	private void initDatas() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getCurMsg();
			}
		}).start();
	}
	
	private void getCurMsg() {
		CurMsgService cms = new CurMsgService(this);
		Message msg = handler.obtainMessage(NEWEST);
		msg.obj = cms.getCurrentMessages();
		msg.sendToTarget();		
	}

	@Override
	public void onFooterRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				getNewestMsg();
			}
		}).start();
	}
	
	private void getNewestMsg() {
		AftMsgService ams = new AftMsgService(this);
//		String addTime = msgData.get(msgData.size() - 1).getAddTime();
		Message msg = handler.obtainMessage(FOOTER);
//		msg.obj = ams.getOrderMessages(addTime);
		msg.obj = ams.getOrderMessages("");
		msg.sendToTarget();
	}

	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			@Override
			public void run() {
				getHistoryMsg();
			}
		}).start();
	}
	
	private void getHistoryMsg() {
		BefMsgService bms = new BefMsgService(this);
//		String addTime = msgData.get(0).getAddTime();
		Message msg = handler.obtainMessage(HEADER);
//		msg.obj = bms.getOrderMessages(addTime);
		msg.obj = bms.getOrderMessages("");
		msg.sendToTarget();		
	}

	private class ViewHolder {
		LinearLayout img;
		TextView title;
		MsgTextView msg;
		TextView time;
		ImageView arrow;
	}

	private class MsgAdapter extends ArrayAdapter<OrderMsgItem> {
		public MsgAdapter(Context cx, int resId, List<OrderMsgItem> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.msg_list, null);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position), position);
			return cv;
		}
	}
	
	private void findViews(final ViewHolder holder, View cv) {
		holder.title = (TextView) cv.findViewById(R.id.title);
		holder.msg = (MsgTextView) cv.findViewById(R.id.msg);
		holder.time = (TextView) cv.findViewById(R.id.time);
		holder.img = (LinearLayout) cv.findViewById(R.id.img);
		holder.arrow = (ImageView) cv.findViewById(R.id.arrow);
		holder.arrow.setBackgroundResource(R.drawable.down);
		holder.msg.setMaxLines(2);
		holder.msg.setExpand(false);
		holder.img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("TAG", "aaaaaaaaaaaaaaaaaaa : " + v.getId());
				ViewUtils.expandTextView(holder.msg, holder.arrow);
			}
		});
	}
	
	private void refreshItem(ViewHolder holder, OrderMsgItem msg, int ps) {
		holder.title.setText(getString(R.string.xiao_title) + "" + (ps + 1));
		holder.msg.setText(msg.getContent());
		holder.time.setText(DateUtil.getFormatedDate(msg.getAddtime()));
	}
}