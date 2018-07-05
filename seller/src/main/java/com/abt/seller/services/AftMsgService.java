package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Login;
import com.abt.seller.models.OrderMsg;
import com.abt.seller.utils.HttpUtil;
import android.content.Context;

public class AftMsgService extends BaseHttpService {
	private List<OrderMsg> msgs = new ArrayList<OrderMsg>();

	public AftMsgService(Context context) {
		super(context);
	}

	public List<OrderMsg> getOrderMessages(String addtime) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			if (login != null && login.getAdminId() != -1) {
				String res = HttpUtil.doGet(Constant.AftMsgUri,
						login.getAdminId() + "", addtime);
				return handleRes(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgs;
	}

	private List<OrderMsg> handleRes(String res) throws JSONException {
		JSONObject jr = new JSONObject(res);
		if (jr.optInt("res") == 0) {
			JSONArray da = (JSONArray) jr.get("data");
			for (int i = 0; i < da.length(); i++) {
				JSONObject jo = (JSONObject) da.get(i);
				JSONArray ma = (JSONArray) jo.get("content");
				String at = (String) jo.get("addtime");
				for (int j = 0; j < ma.length(); j++) {
//					JSONObject mo = (JSONObject) ma.get(j);
//					OrderMsg msg = new OrderMsg(mo.optInt("order_id"),
//							mo.optString("content"), at);
//					msgs.add(msg);
				}
			}
		}
		return msgs;
	}
}