package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Login;
import com.abt.seller.models.Type;
import com.abt.seller.utils.HttpUtil;

public class GoodsTypeAddService extends BaseHttpService {
	private List<Type> types = new ArrayList<Type>();

	public GoodsTypeAddService(Context context) {
		super(context);
	}

	public List<Type> addType(String type) {
		Log.d("TAG", "tttttttttttttttttttttttttttt : " + type);
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
//				JSONObject parm = new JSONObject();
//				parm.put("admin_id", login.getAdminId());
				String res = HttpUtil.doGet(Constant.goodsTypeAddUri + "/shopsid/1/typename/" + type + "/");
//						"[[\"admin_id\"," + login.getAdminId() + "]]");
				return handleRes(res);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return types;
	}

	private List<Type> handleRes(String res) throws JSONException {
		Log.d("TAG", "ffffffffffffffff : " + res);
		JSONObject jo = new JSONObject(res);
		if (jo.optInt("res") == 0) {
			JSONArray pa = (JSONArray) jo.get("newtype");
			for (int i = 0; i < pa.length(); i++) {
				JSONObject po = (JSONObject) pa.get(i);
				Type type = new Type(
						po.getInt("id"), po.getInt("shopsid"),
						po.getString("name"), po.getInt("pid"), 
						po.getString("path"), 0, 0);
				types.add(type);
			}
		}
		return types;
	}
}