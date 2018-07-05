package com.abt.seller.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.MoreListItem;
import com.abt.seller.services.UpdateService;
import com.abt.seller.utils.CallPhoneUtil;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
	private static final int TYPE_UP = 1;
	private static final int TYPE_DOWN = 2;
	private List<MoreListItem> dataUp, dataDown;
	private ListView listup, listdown;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initDatas();
		initViews();
	}

	private void initDatas() {
		dataUp = new ArrayList<MoreListItem>();
		dataUp.add(item(-1, "XXX电话:010-555555", "010-555555", R.drawable.call_bg));
		dataUp.add(item(-1, "XXX电话:010-555555", "010-555555", R.drawable.call_bg));
		dataUp.add(item(-1, "XXX电话:010-555555", "010-555555", R.drawable.call_bg));

		dataDown = new ArrayList<MoreListItem>();
		dataDown.add(item(R.drawable.feedback, "意见反馈", R.drawable.more_arrow));
		dataDown.add(item(R.drawable.update, "版本更新", R.drawable.more_arrow));
		dataDown.add(item(R.drawable.about, "关于社商", R.drawable.more_arrow));
		dataDown.add(item(R.drawable.logout, "退出", R.drawable.more_arrow));
	}
	
	public MoreListItem item(int logo, String title, int submenu) {
		return new MoreListItem(logo, title, submenu);
	}
	
	public MoreListItem item(int logo, String title, String phoneNumber, int submenu) {
		return new MoreListItem(logo, title, phoneNumber, submenu);
	}

	private void initViews() {
		listup = (ListView) findViewById(R.id.list_up);
		listup.setAdapter(new MoreListAdapter(this, R.layout.more_list_item, dataUp));
		listup.setOnItemClickListener(upItemClickListener);
		listdown = (ListView) findViewById(R.id.list_down);
		listdown.setAdapter(new MoreListAdapter(this, R.layout.more_list_item, dataDown));
		listdown.setOnItemClickListener(downItemClickListener);
	}
	
	private OnItemClickListener upItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SettingsActivity.this.onItemClick(parent, view, TYPE_UP, position, id);
		}
	};
	
	private OnItemClickListener downItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SettingsActivity.this.onItemClick(parent, view, TYPE_DOWN, position, id);
		}
	};
	
	private void onItemClick(AdapterView<?> parent, View v, int type, int position, long id) {
		switch (type) {
		case TYPE_UP:
			MoreListItem item = dataUp.get(position);
			CallPhoneUtil.callPhone(this, item.getPhoneNumber());
			break;
		case TYPE_DOWN:
			downOnClick(position);
			break;
		}
	}
	
	private void downOnClick(int position) {
		switch (position) {
		case 0:
			startActivity(new Intent(this, FeedbackActivity.class));
			break;
		case 1:
//			Toast.makeText(this, "无更新", Toast.LENGTH_SHORT).show();
			new UpdateService(this).startUpdate();
			break;
		case 2:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case 3:
			loginOutDialog();
			break;
		}
	}

	private void loginOutDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提醒");
		dialog.setMessage("是否退出?");
		dialog.setPositiveButton("确定", positiveClick);
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}
	
	private OnClickListener positiveClick = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			finish();
		}
	};

	private class MoreListAdapter extends ArrayAdapter<MoreListItem> {
		public MoreListAdapter(Context cx, int resId, List<MoreListItem> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder = null;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.more_list_item, null);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			
			if (position == 0) {
				cv.setBackgroundResource(R.drawable.list_above_bg);
			} else if (position == (getCount() - 1)) {
				cv.setBackgroundResource(R.drawable.list_below_bg);
			} else {
				cv.setBackgroundResource(R.drawable.list_mid_bg);
			}
			refreshItem(holder, getItem(position));
			return cv;
		}
	}
	
	private void refreshItem(ViewHolder holder, MoreListItem item) {
		if (item.getLogo() == -1) {
			holder.icon.setVisibility(View.GONE);
		} else {
			holder.icon.setVisibility(View.VISIBLE);
		}
		holder.icon.setImageResource(item.getLogo());
		holder.submenu.setImageResource(item.getSubmenu());
		holder.info.setText(item.getTitle());
	}
	
	private void findViews(ViewHolder holder, View cv) {
		holder.icon = (ImageView) cv.findViewById(R.id.icon);
		holder.info = (TextView) cv.findViewById(R.id.tvInfo);
		holder.submenu = (ImageView) cv.findViewById(R.id.submenu);
	}
	
	private class ViewHolder {
		ImageView icon;
		ImageView submenu;
		TextView info;
	}
}