package com.abt.seller.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.abt.seller.models.Login;

public class LoginManager {
	private static int adminId;
	private static Context context;
	private static LoginManager lm;
	private Login login;

	public static LoginManager getInstance(Context cx) {
		context = cx;
		if (lm == null) {
			lm = new LoginManager();
		}
		return lm;
	}
	
	public void saveLogin(String uanme, int adminId, boolean isRememberPassword) {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString("username", uanme);
		ed.putInt("shopsid", adminId);
		ed.putBoolean("is_remember_password", isRememberPassword);
		ed.commit();
	}

	public void delLogin() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.remove("username");
		ed.remove("shopsid");
		ed.remove("is_remember_password");
		ed.commit();
	}

	public Login getLoginInstance() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String uname = sp.getString("username", null);
		if (uname != null && uname.trim().length() != 0) {
			boolean isRememberPassword = sp.getBoolean("is_remember_password",
					false);
			int adminId = sp.getInt("shopsid", -1);
			login = new Login(uname, adminId, isRememberPassword);
		}
		return login;
	}

	public int getLoginId() {
		SharedPreferences sp = context.getSharedPreferences("login",
				Context.MODE_PRIVATE);
		String uname = sp.getString("username", null);
		if (uname != null && uname.trim().length() != 0) {
			adminId = sp.getInt("shopsid", -1);
		}
		return adminId;
	}
	
	public boolean isLogined() {
		LoginManager lm = getInstance(context);
		Login login = lm.getLoginInstance();
		if (login != null && login.getAdminId() != -1) {
			return true;
		} else {
			return false;
		}
	}
}