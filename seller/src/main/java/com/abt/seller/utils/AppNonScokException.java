package com.abt.seller.utils;

public class AppNonScokException extends Exception {
	private static final long serialVersionUID = 1L;

	public AppNonScokException(String detailMessage) {
		super(detailMessage);
	}

	public AppNonScokException() {
		super();
	}

	public AppNonScokException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public AppNonScokException(Throwable cause) {
		super(cause);
	}
}