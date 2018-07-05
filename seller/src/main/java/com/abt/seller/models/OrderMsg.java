package com.abt.seller.models;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(String totalnum) {
		this.totalnum = totalnum;
	}
	public String getCountnum() {
		return countnum;
	}
	public void setCountnum(String countnum) {
		this.countnum = countnum;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public ArrayList<OrderMsgItem> getInforlist() {
		return inforlist;
	}
	public void setInforlist(ArrayList<OrderMsgItem> inforlist) {
		this.inforlist = inforlist;
	}
	public OrderMsg(int orderId, String msg, String totalnum, String countnum,
			String p, ArrayList<OrderMsgItem> inforlist) {
		super();
		this.orderId = orderId;
		this.msg = msg;
		this.totalnum = totalnum;
		this.countnum = countnum;
		this.p = p;
		this.inforlist = inforlist;
	}
	@Override
	public String toString() {
		return "OrderMsg [orderId=" + orderId + ", msg=" + msg + ", totalnum="
				+ totalnum + ", countnum=" + countnum + ", p=" + p
				+ ", inforlist=" + inforlist + "]";
	}
	private int orderId;
	private String msg;
	private String totalnum;
	private String countnum;
	private String p;
	private ArrayList<OrderMsgItem> inforlist;

}