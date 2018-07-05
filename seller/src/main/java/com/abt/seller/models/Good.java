package com.abt.seller.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Good implements Serializable {
	private static final long serialVersionUID = 1L;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
	public int getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public float getMarketprice() {
		return marketprice;
	}
	public void setMarketprice(float marketprice) {
		this.marketprice = marketprice;
	}
	public float getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(float goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public Good(int id, int shopsid, int goodsnum, String goods, int typeid,
			float marketprice, float goodsprice, String picname,
			String content, int state, String picpath, ArrayList<String> imgs,
			ArrayList<String> imgNames, ArrayList<Type> types) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.goodsnum = goodsnum;
		this.goods = goods;
		this.typeid = typeid;
		this.marketprice = marketprice;
		this.goodsprice = goodsprice;
		this.picname = picname;
		this.content = content;
		this.state = state;
		this.picpath = picpath;
		this.imgs = imgs;
		this.imgNames = imgNames;
		this.types = types;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	@Override
	public String toString() {
		return "Good [id=" + id + ", shopsid=" + shopsid + ", goodsnum="
				+ goodsnum + ", goods=" + goods + ", typeid=" + typeid
				+ ", marketprice=" + marketprice + ", goodsprice=" + goodsprice
				+ ", picname=" + picname + ", content=" + content + ", state="
				+ state + ", picpath=" + picpath + ", imgs=" + imgs
				+ ", imgNames=" + imgNames + ", types=" + types + "]";
	}

	private int id;
	private int shopsid;
	private int goodsnum;
	private String goods;
	private int typeid;
	private float marketprice;
	private float goodsprice;
	private String picname;
	private String content;
	private int state;
	private String picpath;
	private List<String> imgs = new ArrayList<String>();
	private List<String> imgNames = new ArrayList<String>();
	private ArrayList<Type> types = new ArrayList<Type>();

	public List<String> getImgNames() {
		return imgNames;
	}
	public void setImgNames(List<String> imgNames) {
		this.imgNames = imgNames;
	}
	public ArrayList<Type> getTypes() {
		return types;
	}
	public void setTypes(ArrayList<Type> types) {
		this.types = types;
	}
}