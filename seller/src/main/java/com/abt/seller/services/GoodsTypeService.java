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

public class GoodsTypeService extends BaseHttpService {
	private List<Type> types = new ArrayList<Type>();

	public GoodsTypeService(Context context) {
		super(context);
	}

	public List<Type> getTypes() {
		Log.d("TAG", "getTypes dddddddddddddddddddddddddddddd");
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
//				JSONObject parm = new JSONObject();
//				parm.put("admin_id", login.getAdminId());
				String res = HttpUtil.doGet(Constant.goodsTypeUri + "/shopsid/1");
//						"[[\"admin_id\"," + login.getAdminId() + "]]");
				return handleRes(res);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return types;
	}

	private List<Type> handleRes(String res) throws JSONException {
		Log.d("TAG", "res aaaaaa bbbbbbbbb ccccccccccc : " + res);
		JSONObject jo = new JSONObject(res);
		if (jo.optInt(res) == 0) {
			JSONArray pb = (JSONArray) jo.get("typelist");
			for (int i = 0; i < pb.length(); i++) {
				JSONObject jso = (JSONObject) pb.getJSONObject(i);
				int id = jso.getInt("id");
				int shopsid = jso.getInt("shopsid");
				String name = jso.getString("name");
//				int pid = jso.getInt("pid");
				int pid = 1;
				String path = jso.getString("path");
				Type type = new Type(id, shopsid, name, pid, path, 0, 0);
				types.add(type);
				Log.d("TAG", "type abccccccccccccccccccc : " + name);
				Log.d("TAG", "type abccccccccccccccccccc id : " + id);
			}
		}
		return types;
	}
}