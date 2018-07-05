package com.abt.seller.services;

import org.json.JSONObject;
import android.content.Context;
import com.abt.seller.models.Constant;
import com.abt.seller.utils.HttpUtil;

public class MsgNumService extends BaseHttpService {
	private int num = -1;

	public MsgNumService(Context context) {
		super(context);
	}

	public int getMsgNum() {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			if (lm.isLogined()) {
				String res = HttpUtil.doGet(Constant.msgCountUri,
						"[[\"shopsid\"," + lm.getLoginId() + "]]");
				if (res != null) {
					JSONObject jr = new JSONObject(res);
					if (jr.optInt("res") == 0) {
						num = jr.optInt("num");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
}