package com.abt.seller.models;

import java.util.List;

public class Order {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public List<OrderGood> getOrderGood() {
		return orderGood;
	}
	public void setOrderGood(List<OrderGood> orderGood) {
		this.orderGood = orderGood;
	}
	public Order(int id, String ordernum, int shopsid, int uid, String linkman,
			String address, String phone, float total, int status, String addtime, List<OrderGood> orderGood) {
		super();
		this.id = id;
		this.ordernum = ordernum;
		this.shopsid = shopsid;
		this.uid = uid;
		this.linkman = linkman;
		this.address = address;
		this.phone = phone;
		this.total = total;
		this.status = status;
		this.addtime = addtime;
		this.orderGood = orderGood;
	}
	
	private int id;
	private String ordernum;
	private int shopsid;
	private int uid;
	private String linkman;
	private String address;
	private String phone;
	private float total;
	private int status;
	private String addtime;
	private List<OrderGood> orderGood;
}