package com.abt.seller.activitys;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.Constant;
import com.abt.seller.services.LoginManager;
import com.abt.seller.services.MsgNumService;
import com.abt.seller.utils.JPushUtil;
import com.abt.seller.utils.SystemUtil;
import com.abt.seller.views.BadgeView;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@Suppreabtarnings("deprecation")
public class TabsActivity extends TabActivity implements TagAliasCallback,
		OnCheckedChangeListener {
	public static BadgeView badgeView;
	public static String uId;
	private static final int BADGE_REFRESH = 0;
	private static final int PAGE_NUM = 4;
	private static final String TAB_ORDERS 	 = "tab_tag_orders";
	private static final String TAB_GOODS 	 = "tab_tag_goods";
	private static final String TAB_MSG 	 = "tab_tag_messages";
	private static final String TAB_SETTINGS = "tab_tag_settings";
	private TabHost host;
	private RadioGroup group;
	private TextView badge;
	private LoginManager lm;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BADGE_REFRESH:
				refreshBadge(msg.arg1);
				break;
			}
		}
	};
	
	private void refreshBadge(int num) {
		if (num > 0) {
			badgeView.show();
			badgeView.setText(num + "");
		} else {
			badgeView.hide();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		initViews();
		initDatas();
	}

	private void initViews() {
		lm = LoginManager.getInstance(this);
		host = getTabHost();
		host.addTab(tab(TAB_ORDERS, R.string.tab_order, R.drawable.dd, OrdersActivity.class));
		host.addTab(tab(TAB_GOODS, R.string.tab_good, R.drawable.sp, GoodsActivity.class));
		host.addTab(tab(TAB_MSG, R.string.tab_msg, R.drawable.xx, MsgActivity.class));
		host.addTab(tab(TAB_SETTINGS, R.string.tab_setting, R.drawable.setting, SettingsActivity.class));
		group = (RadioGroup) findViewById(R.id.tabs_group);
		group.setOnCheckedChangeListener(this);
		((RadioButton) group.getChildAt(0)).setChecked(true);

		badge = (TextView) findViewById(R.id.badge_view);
		badge.setWidth(SystemUtil.getWidth(this) / (PAGE_NUM - 2));
		badgeView = new BadgeView(this, badge);
		badgeView.hide();
	}

	private TabSpec tab(String tag, int label, int icon, Class<?> cs) {
		TabSpec ts = host.newTabSpec(tag);
		Intent intent = new Intent(this, cs);
		intent.putExtra("shopsid", lm.getLoginId());
		ts.setContent(intent);
		ts.setIndicator(getString(label), getResources().getDrawable(icon));
		return ts;
	}
	
	private void initDatas() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getMsgNum();
			}
		}).start();
		initJPush();
	}
	
	private void getMsgNum() {
		MsgNumService mcs = new MsgNumService(this);
		Message msg = handler.obtainMessage(BADGE_REFRESH);
		msg.arg1 = mcs.getMsgNum();
		msg.sendToTarget();		
	}
	
	private void initJPush() {
		uId = "" + getIntent().getIntExtra("shopsid", 6);
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.add(Constant.SELLERID + uId);
		JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_button1:
			host.setCurrentTabByTag(TAB_ORDERS);
			break;
		case R.id.radio_button2:
			host.setCurrentTabByTag(TAB_GOODS);
			break;
		case R.id.radio_button3:
			host.setCurrentTabByTag(TAB_MSG);
			break;
		case R.id.radio_button4:
			host.setCurrentTabByTag(TAB_SETTINGS);
			break;
		}
	}

	@Override
	public void gotResult(int code, String alias, Set<String> tags) {
		StringBuilder sb = new StringBuilder();
		sb.append("Set tag and alias ");
		switch (code) {
		case 0:
			sb.append("success, alias = " + alias);
			sb.append("; tags = " + tags);
			break;
		default:
			sb.append("failed with errorCode = " + code);
			sb.append(", alias = " + alias);
			sb.append("; tags = " + tags);
		}
		JPushUtil.showToast(sb.toString(), getApplicationContext());
	}
}