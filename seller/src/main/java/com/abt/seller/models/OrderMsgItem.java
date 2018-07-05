package com.abt.seller.models;

import java.io.Serializable;

public class OrderMsgItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInfortype() {
		return infortype;
	}
	public void setInfortype(int infortype) {
		this.infortype = infortype;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getShopsid() {
		return shopsid;
	}
	public void setShopsid(int shopsid) {
		this.shopsid = shopsid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public OrderMsgItem(int id, int infortype, int orderid, int uid,
			String username, int shopsid, String title, String content,
			String addtime) {
		super();
		this.id = id;
		this.infortype = infortype;
		this.orderid = orderid;
		this.uid = uid;
		this.username = username;
		this.shopsid = shopsid;
		this.title = title;
		this.content = content;
		this.addtime = addtime;
	}
	@Override
	public String toString() {
		return "OrderMsgItem [id=" + id + ", infortype=" + infortype
				+ ", orderid=" + orderid + ", uid=" + uid + ", username="
				+ username + ", shopsid=" + shopsid + ", title=" + title
				+ ", content=" + content + ", addtime=" + addtime + "]";
	}
	private int id;
	private int infortype;
	private int orderid;
	private int uid;
	private String username;
	private int shopsid;
	private String title;
	private String content;
	private String addtime;
	
}