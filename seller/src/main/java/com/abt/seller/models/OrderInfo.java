package com.abt.seller.models;

public class OrderInfo {
	private int orderId;
	private int shopId;
	private int goodNumber;
	private long confirmTime;
	private float price;
	private String orderSn;
	private String goodsName;
	private String consignee;
	private String mobile;
	private String address;
	private String status;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodNumber() {
		return goodNumber;
	}

	public void setGoodNumber(int goodNumber) {
		this.goodNumber = goodNumber;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OrderInfo(int orderId, int shopId, String orderSn, long confirmTime,
			String goodsName, int goodNumber, float price, String consignee,
			String mobile, String address, String status) {
		super();
		this.orderId = orderId;
		this.shopId = shopId;
		this.orderSn = orderSn;
		this.confirmTime = confirmTime;
		this.goodsName = goodsName;
		this.goodNumber = goodNumber;
		this.price = price;
		this.consignee = consignee;
		this.mobile = mobile;
		this.address = address;
		this.status = status;
	}
}