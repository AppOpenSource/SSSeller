package com.abt.seller.utils;

public class QNonScokException extends Exception {
	private static final long serialVersionUID = 1L;

	public QNonScokException(String detailMessage) {
		super(detailMessage);
	}

	public QNonScokException() {
		super();
	}

	public QNonScokException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public QNonScokException(Throwable cause) {
		super(cause);
	}
}