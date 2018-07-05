package com.abt.seller.models;

public class OrderGood {

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
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public OrderGood(int id, int shopsid, String goods, String picname,
			String picpath, String content) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.goods = goods;
		this.picname = picname;
		this.picpath = picpath;
		this.content = content;
	}
	@Override
	public String toString() {
		return "OrderGood [id=" + id + ", shopsid=" + shopsid + ", goods="
				+ goods + ", picname=" + picname + ", picpath=" + picpath
				+ ", content=" + content + "]";
	}
	private int id;
	private int shopsid;
	private String goods;
	private String picname;
	private String picpath;
	private String content;

}