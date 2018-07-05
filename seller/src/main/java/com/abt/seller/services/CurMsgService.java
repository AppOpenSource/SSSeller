package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Login;
import com.abt.seller.models.OrderMsgItem;
import com.abt.seller.utils.HttpUtil;

public class CurMsgService extends BaseHttpService {
//	private List<OrderMsg> msgs = new ArrayList<OrderMsg>();
	private List<OrderMsgItem> msgItems = new ArrayList<OrderMsgItem>();

	public CurMsgService(Context context) {
		super(context);
	}

	public List<OrderMsgItem> getCurrentMessages() {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			if (login != null && login.getAdminId() != -1) {
				String res = HttpUtil.doGet(Constant.curMsgUri + "/shopsid/" + login.getAdminId());
//						"[[\"shopsid\"," + login.getAdminId() + "]]");
				return handleRes(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgItems;
	}

	private List<OrderMsgItem> handleRes(String res) throws JSONException {
		JSONObject jr = new JSONObject(res);
		if (jr.optInt("res") == 0) {
			JSONArray da = (JSONArray) jr.get("inforlist");
			for (int i = 0; i < da.length(); i++) {
				JSONObject jo = (JSONObject) da.get(i);
				int id = jo.getInt("id");
				int infortype = jo.getInt("infortype");
				int orderid = jo.getInt("orderid");
				int uid = jo.getInt("uid");
				String username = (String) jo.get("username");
				int shopsid = jo.getInt("shopsid");
				String title = (String) jo.get("title");
				String content = (String) jo.get("content");
				String addtime = (String) jo.get("addtime");
				OrderMsgItem msgItem = new OrderMsgItem(id, infortype, orderid, 
						uid, username, shopsid, title, content, addtime);
				msgItems.add(msgItem);
			}
		}
		return msgItems;
	}
}