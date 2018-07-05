package com.abt.seller.models;

import java.io.Serializable;

public class Type implements Serializable {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Type [id=" + id + ", shopsid=" + shopsid + ", name=" + name
				+ ", pid=" + pid + ", path=" + path + ", state=" + state
				+ ", type=" + type + "]";
	}
	public Type(int id, int shopsid, String name, int pid, String path,
			int state, int type) {
		super();
		this.id = id;
		this.shopsid = shopsid;
		this.name = name;
		this.pid = pid;
		this.path = path;
		this.state = state;
		this.type = type;
	}
	private int id;
	private int shopsid;
	private String name;
	private int pid;
	private String path;
	private int state; // 0 normal, 1 pressed
	private int type; // 0 normal, 1 add
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
