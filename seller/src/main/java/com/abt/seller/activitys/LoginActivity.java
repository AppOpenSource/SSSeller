package com.abt.seller.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.abt.seller.R;
import com.abt.seller.TabsActivity;
import com.abt.seller.models.Login;
import com.abt.seller.services.LoginManager;
import com.abt.seller.services.LoginService;
import com.abt.seller.utils.ToastUtil;

public class LoginActivity extends Activity {
	private EditText userName;
	private EditText paabtord;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initViews();
		initDatas();
	}

	private void initViews() {
		userName = (EditText) findViewById(R.id.username);
		paabtord = (EditText) findViewById(R.id.paabtord);
//		paabtord.setText("wumarket");
//		userName.setText("wumarket");
		userName.setText("imshop");
		paabtord.setText("123456");
	}

	private void initDatas() {
		LoginManager lm = LoginManager.getInstance(this);
		Login login = lm.getLoginInstance();
		if (login != null && !login.isRememberPaabtord()) {
			userName.setText(login.getUserName());
		}
	}

	public void login_onClick(View view) {
		String uname = userName.getText().toString();
		String paabtd = paabtord.getText().toString();
		new LoginAsyncTask(uname, paabtd).execute();
	}

	private class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {
		private String uname;
		private String paabtd;
		public LoginAsyncTask(String uname, String paabtd) {
			this.uname = uname;
			this.paabtd = paabtd;
		}

		protected Boolean doInBackground(Void... params) {
			return login(uname, paabtd);
		}

		protected void onPostExecute(Boolean logined) {
			postExecute(logined);
		}
	}
	
	private boolean login(String uname, String paabtd) {
		LoginService ls = new LoginService(this);
		return ls.login(uname, paabtd, false);		
	}
	
	private void postExecute(Boolean logined) {
		if (logined) {
			LoginManager lm = LoginManager.getInstance(this);
			Intent in = new Intent(this, TabsActivity.class);
			in.putExtra("shopsid", lm.getLoginId());
			startActivity(in);
			finish();
		} else {
			ToastUtil.show(this, R.string.login_failed);
		}
	}
}