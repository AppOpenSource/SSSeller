package com.abt.seller.models;

public class GoodOutline {
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}
	public int getGoodsnum() {
		return goodsnum;
	}
	public void setGoodsnum(int goodsnum) {
		this.goodsnum = goodsnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public GoodOutline(int orderid, int goodsid, int goodsnum, String name,
			float price, int num) {
		super();
		this.orderid = orderid;
		this.goodsid = goodsid;
		this.goodsnum = goodsnum;
		this.name = name;
		this.price = price;
		this.num = num;
	}
	@Override
	public String toString() {
		return "GoodOutline [orderid=" + orderid + ", goodsid=" + goodsid
				+ ", goodsnum=" + goodsnum + ", name=" + name + ", price="
				+ price + ", num=" + num + "]";
	}
	private int orderid;
	private int goodsid;
	private int goodsnum;
	private String name;
	private float price;
	private int num;
}