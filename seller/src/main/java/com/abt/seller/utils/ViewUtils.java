package com.abt.seller.utils;

import com.abt.seller.R;
import com.abt.seller.views.MsgTextView;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ViewUtils {
	private static ExpandHandler expandHandler;

	public static void expandTextView(MsgTextView tv, ImageView img) {
		boolean isExpand;
		if (tv.isExpand()) {
			isExpand = true;
		} else {
			isExpand = false;
		}
		if (expandHandler == null || (expandHandler != null && expandHandler.expand != tv)) {
			expandHandler = new ExpandHandler(tv, isExpand, img);
		}
		expandHandler.sendEmptyMessage(1);
	}

	private static class ExpandHandler extends Handler {
		private MsgTextView expand;
		private ImageView mImg;
		private int CollapseHeight;
		private int ExpandHeight;
		private int NormalHeight;
		private int currentHeight;
		private int distance = 10;
		private int timeDelay = 2;
		private boolean isExpand;

		ExpandHandler(MsgTextView tv, boolean yes, ImageView img) {
			expand = tv;
			CollapseHeight = expand.getMeasuredHeight();
			int lines = expand.getLineCount();
			int lineHeight = expand.getLineHeight();
			ExpandHeight = lineHeight * lines;
			NormalHeight = lineHeight * 2;
//			int maxLine = expand.getMaxLines();
//			int errorHeight = CollapseHeight - lineHeight * maxLine;
//			ExpandHeight = lineHeight * lines + errorHeight;
			isExpand = yes;
			mImg = img;
		}

		@Override
		public void handleMessage(Message msg) {
			currentHeight = expand.getMeasuredHeight();
			if (isExpand) {
				Log.d("TAG", " 收起");
				Log.d("TAG", " 收起 currentHeight : " + currentHeight);
				Log.d("TAG", " 收起 CollapseHeight : " + CollapseHeight);
				Log.d("TAG", " 收起 distance : " + distance);
				CollapseHeight = 56;
				if (currentHeight > CollapseHeight + distance) {
//					expand.setHeight(currentHeight - distance);
//					this.sendEmptyMessageDelayed(1, timeDelay);
//				} else {
//					expand.setHeight(CollapseHeight);
					mImg.setBackgroundResource(R.drawable.down);
					expand.setHeight(NormalHeight);
					Log.d("TAG", " 收起 ： "+ NormalHeight);
					isExpand = !isExpand;
					expand.setExpand(isExpand);
				}
			} else {
				Log.d("TAG", " 打开");
				Log.d("TAG", " 打开 currentHeight ： " + currentHeight);
				Log.d("TAG", " 打开 ExpandHeight :" + ExpandHeight);
				Log.d("TAG", " 打开 distance :" + distance);
				if (currentHeight < ExpandHeight - distance) {
//					expand.setHeight(currentHeight + distance);
//					this.sendEmptyMessageDelayed(1, timeDelay);
//				} else if (currentHeight < ExpandHeight) {
					mImg.setBackgroundResource(R.drawable.up);
					expand.setHeight(ExpandHeight);
					Log.d("TAG", " 打开：" + ExpandHeight);
					isExpand = !isExpand;
					expand.setExpand(isExpand);
				}
			}
		}
	}
}