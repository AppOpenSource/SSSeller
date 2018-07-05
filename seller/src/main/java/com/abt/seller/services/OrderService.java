package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Login;
import com.abt.seller.models.OrderInfo;
import com.abt.seller.utils.HttpUtil;

public class OrderService extends BaseHttpService {
	private List<OrderInfo> orders = new ArrayList<OrderInfo>();
	
	public OrderService(Context context) {
		super(context);
	}

	public List<OrderInfo> getOrders() {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			if (login != null && login.getAdminId() != -1) {
				String res = HttpUtil.doGet(Constant.orderUri,
						"[[\"admin_id\",\"" + login.getAdminId() + "\"]]");
				return handleRes(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	private List<OrderInfo> handleRes(String res) throws JSONException {
		JSONObject jo = new JSONObject(res);
		if (jo.getInt("res") == 0) {
			JSONArray oa = (JSONArray) jo.get("data");
			for (int i = 0; i < oa.length(); i++) {
				JSONObject oo = oa.getJSONObject(i);
				OrderInfo order = new OrderInfo(oo.getInt("order_id"),
						oo.getInt("shop_id"), oo.getString("order_sn"),
						oo.getLong("confirm_time"), oo.getString("goods_name"),
						oo.getInt("goods_number"),
						(float) (oo.getDouble("price")),
						oo.getString("consignee"), oo.getString("mobile"),
						oo.getString("address"), oo.getString("status"));
				orders.add(order);
			}
		}
		return orders;
	}
}