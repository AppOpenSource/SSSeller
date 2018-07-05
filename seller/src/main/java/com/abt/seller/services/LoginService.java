package com.abt.seller.services;

import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.abt.seller.models.Constant;
import com.abt.seller.utils.HttpUtil;

public class LoginService extends BaseHttpService {

	public LoginService(Context context) {
		super(context);
	}

	public boolean login(String usrName, String passwd,
			boolean isRememberPassword) {
		Log.d("TAG", "login");
		try {
			JSONObject jo = new JSONObject();
			jo.put("username", usrName);
			jo.put("pass", passwd);
			String res = HttpUtil.doPost(Constant.loginUri,
					"[[\"parm\"," + jo.toString() + "]]");
			Log.d("TAG", "rj.getInt(res) : " + res);
			JSONObject rj = new JSONObject(res);
			Log.d("TAG", "rj.getInt(res) : " + rj.getInt("res"));
			if (rj.getInt("res") == 0) {
				int adminId = rj.getInt("shopsid");
				LoginManager lm = LoginManager.getInstance(context);
				lm.saveLogin(usrName, adminId, isRememberPassword);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("TAG", "" + e.toString());
		}
		return false;
	}
}