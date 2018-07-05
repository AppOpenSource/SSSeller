package com.abt.seller.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.imgload.ImgLoad;
import com.abt.seller.utils.ImageUtil;

import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("deprecation")
public class GalleryActivity extends Activity {
	private int imgPosition;
	private int imgListSize;
	private ImageAdapter imgAdapter;
	private Gallery gallery;
	private ImgLoad loader;
	private TextView toast;
	private String imgType;
	private List<String> imgList;
	private HashMap<Integer, Bitmap> dateCache = new HashMap<Integer, Bitmap>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		initDatas();
		initViews();
	}

	private void initDatas() {
		Intent in = getIntent();
		imgList = in.getStringArrayListExtra("image_list");
		imgType = in.getStringExtra("image_type");
		imgPosition = in.getIntExtra("image_position", 0);
		imgListSize = imgList.size();
	}

	private void initViews() {
		toast = (TextView) findViewById(R.id.toast);
		imgAdapter = new ImageAdapter(this, R.layout.gallery_imageitem, imgList);
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(imgAdapter);
		gallery.setGravity(Gravity.CENTER_HORIZONTAL);
		gallery.setSelection(imgPosition);
		gallery.setOnItemClickListener(clickListener);
		gallery.setOnItemSelectedListener(selectedListener);
		gallery.setUnselectedAlpha(0f);
		gallery.setSpacing(25);
	}

	private OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			int son = (position + 1) % imgListSize;
			if (son == 0) son = imgListSize;
			toast.setText(son + "/" + imgListSize);
		}
	};

	private OnItemSelectedListener selectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			int son = (position + 1) % imgListSize;
			if (son == 0) son = imgListSize;
			toast.setText(son + "/" + imgListSize);
			releaseBitmap();
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) { }
	};

	private class ImageAdapter extends ArrayAdapter<String> {
		public ImageAdapter(Context cx, int resId, List<String> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder = null;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.gallery_imageitem, null);
				holder = new ViewHolder();
				holder.img = (ImageView) cv.findViewById(R.id.image_view);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(getItem(position % imgListSize), holder, position, loader);
			return cv;
		}
	}
	
	private void refreshItem(String imgPath, ViewHolder holder, int ps, ImgLoad loader) {
		holder.img.setScaleType(ImageView.ScaleType.CENTER);
		holder.img.setBackgroundColor(Color.alpha(1));

		if (imgType.equalsIgnoreCase("remote")) {
			loader.addTask(imgPath, holder.img);
			loader.doTask();
		} else if (imgType.equalsIgnoreCase("local")) {
			Bitmap cur = (Bitmap) dateCache.get(ps);
			if (cur != null) {
				holder.img.setImageBitmap(cur);
			} else {
				cur = getPhotoItem(imgPath, 4);
				holder.img.setImageBitmap(cur);
				dateCache.put(ps, cur);
			}
		}
	}
	
	private class ViewHolder {
		ImageView img;
	}

	private void releaseBitmap() {
		int start = gallery.getFirstVisiblePosition() - 3;
		int end = gallery.getLastVisiblePosition() + 3;
		Bitmap bmp;
		for (int del = 0; del < start; del++) {
			bmp = (Bitmap) dateCache.get(del);
			if (bmp != null) {
				dateCache.remove(del);
				bmp.recycle();
			}
		}
		freeBitmapFromIndex(end);
	}

	private void freeBitmapFromIndex(int end) {
		Bitmap bmp;
		for (int del = end + 1; del < dateCache.size(); del++) {
			bmp = (Bitmap) dateCache.get(del);
			if (bmp != null) {
				dateCache.remove(del);
				bmp.recycle();
			}
		}
	}

	public Bitmap getPhotoItem(String filepath, int size) {
		Bitmap bmp = ImageUtil.getBitmap(filepath, size);
		bmp = Bitmap.createScaledBitmap(bmp, 640, 480, true);
		return bmp;
	}
}