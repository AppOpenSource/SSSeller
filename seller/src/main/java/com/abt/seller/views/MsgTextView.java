package com.abt.seller.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MsgTextView extends TextView {  
	
	private boolean isExpand;
	
    public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	public MsgTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
}