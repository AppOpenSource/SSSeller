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
import com.abt.seller.models.Type;
import com.abt.seller.utils.HttpUtil;

public class GoodDetailService extends BaseHttpService {
	private Good good;

	public GoodDetailService(Context context) {
		super(context);
	}

	public boolean onSale(int goodsId, int isSale) {
		try {
			String res = HttpUtil.doGet(Constant.goodsOperateUri,
					"[[\"goods_id\"," + goodsId + "],[\"issale\"," + isSale + "]]");
			if (new JSONObject(res).getInt("res") == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(int goodsId) {
		try {
			String res = HttpUtil.doGet(Constant.goodsDelUri, "[[\"goods_id\"," + goodsId + "]]");
			if (new JSONObject(res).getInt("res") == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean save(int id, int shopsid, int typeid, String goodsnum, 
			String goods, float goodsprice,	String content, String picname, int state) {
		try {
			JSONObject jo = new JSONObject();
//			LoginManager lm = LoginManager.getInstance(context);
//			Login login = lm.getLoginInstance();
//			jo.put("admin_id", login.getAdminId());
			jo.put("id", id);
			jo.put("shopsid", shopsid);
			jo.put("typeid", typeid);
			jo.put("goodsnum", goodsnum);
			jo.put("goods", goods);
			jo.put("goodsprice", goodsprice);
			jo.put("content", content);
			jo.put("picname", picname);
			jo.put("state", state);
			Log.d("TAG", "jokkkkkkkkkkkk : " + jo.toString());
			String res = HttpUtil.doPost(Constant.goodsSaveUri, "parm",	jo.toString());
			if (new JSONObject(res).getInt("res") == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean saveGood(String image, String goodsName, int shopsid, int typeid,
			String content, float price, String goodsnum) {
		try {
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
			JSONObject jo = new JSONObject();
//			jo.put("shopsid", login.getAdminId());
			jo.put("shopsid", shopsid);
			jo.put("typeid", typeid);
			jo.put("goodsnum", goodsnum);
			jo.put("goods", goodsName);
			jo.put("goodsprice", price);
			jo.put("content", content);
			jo.put("picname", image);
			String res = HttpUtil.doPost(Constant.goodsSaveUri, "parm", jo.toString());
			if (new JSONObject(res).getInt("res") == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Good getGoodDetail(int goodsId, int shopsid) {
		Log.d("TAG", "getGoodDetail : " + goodsId);
		try {
//			String res = HttpUtil.doGet(Constant.goodsDetailUri, "[[\"goods_id\"," + goodsId + "]]");
			String res = HttpUtil.doGet(Constant.goodsDetailUri + "/id/" + goodsId + "/shopsid/" + shopsid);
			return handleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return good;
	}

	private Good handleRes(String res) throws JSONException {
		Log.d("TAG", "res bbbbbbbbbb : " + res);
		JSONObject jo = new JSONObject(res);
		if (jo.getInt("res") == 0) {
			JSONArray pa = (JSONArray) jo.get("goodsvo");
			JSONObject po = (JSONObject) pa.get(0);
			List<String> imgList = new ArrayList<String>();
			List<String> imgNameList = new ArrayList<String>();
			List<Type> typeList = new ArrayList<Type>();
			if (!po.get("imglist").equals(null)) {
				JSONArray ia = po.getJSONArray("imglist");
				for (int i = 0; i < ia.length(); i++) {
					JSONObject jso = ia.getJSONObject(i);
					String imgName = jso.getString("picname");
					String imgUri = jso.getString("picpath");
					imgNameList.add(imgName);
					imgList.add(imgUri);
				}
			}
			
			JSONArray pb = (JSONArray) jo.get("goodstype");
			for (int i = 0; i < pb.length(); i++) {
				JSONObject jso = (JSONObject) pb.getJSONObject(i);
				int id = jso.getInt("id");
				int shopsid = jso.getInt("shopsid");
				String name = jso.getString("name");
				int pid = jso.getInt("pid");
				String path = jso.getString("path");
				Type type = new Type(id, shopsid, name, pid, path, 0, 0);
				typeList.add(type);
				Log.d("TAG", "type abccccccccccccccccccc : " + name);
			}
			good = new Good(po.getInt("id"), po.getInt("shopsid"), po.getInt("goodsnum"), 
					po.getString("goods"), po.getInt("typeid"),	(float)23.0, 
					(float)po.getDouble("goodsprice"), po.getString("picname"), po.getString("content"), 
					po.getInt("state"),	po.getString("picpath"), (ArrayList)imgList, (ArrayList)imgNameList,
					(ArrayList)typeList);
		}
		return good;
	}
}