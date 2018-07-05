package com.abt.seller.models;

import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<GoodOutline> getDetail() {
		return detail;
	}
	public void setDetail(List<GoodOutline> detail) {
		this.detail = detail;
	}
	public OrderDetail() {}
	public OrderDetail(String ordernum, String linkman, String phone,
			String address, float total, int addtime, int status,
			List<GoodOutline> detail, int shopsid) {
		super();
		this.ordernum = ordernum;
		this.linkman = linkman;
		this.phone = phone;
		this.address = address;
		this.total = total;
		this.addtime = addtime;
		this.status = status;
		this.detail = detail;
		this.shopsid = shopsid;
	}
	@Override
	public String toString() {
		return "OrderDetail [ordernum=" + ordernum + ", linkman=" + linkman
				+ ", phone=" + phone + ", address=" + address + ", total="
				+ total + ", addtime=" + addtime + ", status=" + status
				+ ", detail=" + detail + ", shopsid=" + shopsid + "]";
	}
	private String ordernum;
	private String linkman;
	private String phone;
	private String address;
	private float total;
	private int addtime;
	private int status;
	private List<GoodOutline> detail = new ArrayList<GoodOutline>();
	private int shopsid;
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
}