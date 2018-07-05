package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.abt.seller.models.Constant;
import com.abt.seller.models.GoodOutline;
import com.abt.seller.models.OrderDetail;
import com.abt.seller.utils.HttpUtil;

public class OrderDetailService extends BaseHttpService {
	private OrderDetail orderDetail = new OrderDetail();

	public OrderDetailService(Context context) {
		super(context);
	}

	public OrderDetail getOrderDetail(int orderId) { // 订单编号
		try {
			String res = HttpUtil.doGet(Constant.orderDetailUri + "/orderid/" + orderId);
//					"[[\"order_id\"," + orderId + "]]");
			return handleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderDetail;
	}

	private OrderDetail handleRes(String res) throws JSONException {
		JSONObject jo = new JSONObject(res);
		List<GoodOutline> details = new ArrayList<GoodOutline>();
		if (jo.getInt("res") == 0) {
			Log.d("TAG", "data : " + jo.getString("data"));
			JSONArray jso = jo.getJSONArray("data");
			for (int j = 0; j < jso.length(); j++) {
				JSONObject oa = jso.getJSONObject(j);
				JSONArray jsa = oa.getJSONArray("detail");
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject dObj = jsa.getJSONObject(i);
					GoodOutline detail = new GoodOutline(
							dObj.getInt("orderid"), dObj.getInt("goodsid"),
							dObj.getInt("goodsnum"), dObj.getString("name"),
							(float) (dObj.getDouble("price")), dObj.getInt("num"));
					detail.toString();
					details.add(detail);
				}
				orderDetail.getDetail().addAll(details);
				orderDetail.setOrdernum(oa.getString("ordernum"));
				orderDetail.setAddtime(oa.getInt("addtime"));
				orderDetail.setStatus(oa.optInt("status"));
				orderDetail.setLinkman(oa.getString("linkman"));
				orderDetail.setPhone(oa.getString("phone"));
				orderDetail.setAddress(oa.getString("address"));
				orderDetail.setShopsid(oa.getInt("shopsid"));
			}
		}
		return orderDetail;
	}
}