package com.abt.seller.services;

import android.content.Context;

public class BaseHttpService {
	protected Context context;

	public BaseHttpService(Context context) {
		super();
		this.context = context;
	}
}