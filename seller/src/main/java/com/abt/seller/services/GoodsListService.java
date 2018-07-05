package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.abt.seller.models.Constant;
import com.abt.seller.models.Good;
import com.abt.seller.models.Login;
import com.abt.seller.utils.HttpUtil;

public class GoodsListService extends BaseHttpService {
	private List<Good> goods = new ArrayList<Good>();

	public GoodsListService(Context context) {
		super(context);
	}

	public List<Good> getGoods() {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
//				JSONObject parm = new JSONObject();
//				parm.put("admin_id", login.getAdminId());
				String res = HttpUtil.doGet(Constant.goodsListUri + "/shopsid/1");
//						"[[\"admin_id\"," + login.getAdminId() + "]]");
				return handleRes(res);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}
	
	public List<Good> getGoodsByType(int type, int status) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
//				JSONObject parm = new JSONObject();
//				parm.put("admin_id", login.getAdminId());
				String res = HttpUtil.doGet(Constant.goodsListUri + "/shopsid/1/typeid/" + type + "/state/" + status);
//						"[[\"admin_id\"," + login.getAdminId() + "]]");
				return handleRes(res);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}
	
	public List<Good> getGoodsByStatus(int status, int type, String keyword) {
		Log.d("TAG", "getGoods dddddddddddddddddddddddddddddd");
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
//				JSONObject parm = new JSONObject();
//				parm.put("admin_id", login.getAdminId());
				String res = HttpUtil.doGet(Constant.goodsListUri + "/shopsid/1/typeid/" + type + "/state/" + status + "/keyword/" + keyword);
//						"[[\"admin_id\"," + login.getAdminId() + "]]");
				return handleRes(res);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return goods;
	}

	private List<Good> handleRes(String res) throws JSONException {
		Log.d("TAG", "res aaaaaa : " + res);
		JSONObject jo = new JSONObject(res);
		if (jo.optInt("res") == 0) {
			JSONArray pa = (JSONArray) jo.get("goodslist");
			for (int i = 0; i < pa.length(); i++) {
				JSONObject po = (JSONObject) pa.get(i);
				Good good = new Good(po.getInt("id"), po.getInt("shopsid"),
						po.getInt("goodsnum"), po.getString("goods"), 
						po.getInt("typeid"), (float)po.getDouble("marketprice"), 
						(float)po.getDouble("goodsprice"), po.getString("picname"), 
						po.getString("content"), po.getInt("state"), po.getString("picpath"), 
						new ArrayList(), new ArrayList(), new ArrayList());
				goods.add(good);
			}
		}
		return goods;
	}
}