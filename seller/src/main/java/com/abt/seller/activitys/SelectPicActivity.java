package com.abt.seller.activitys;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;

import com.abt.seller.R;
import com.abt.seller.models.Constant;
import com.abt.seller.utils.SDCardUtil;
import com.abt.seller.utils.ToastUtil;

public class SelectPicActivity extends Activity {
	private String picPath;
	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pic);
	}

	public void take_photo_onClick(View view) {
		if (SDCardUtil.isSDCardAvailable()) {
			ContentResolver cr = getContentResolver();
			photoUri = cr.insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
			Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			in.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(in, Constant.SELECT_PIC_BY_TACK_PHOTO);
		} else {
			ToastUtil.show(this, R.string.sd_not_exit);
		}
	}

	public void pick_photo_onClick(View view) {
		Intent in = new Intent();
		in.setType("image/*");
		in.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(in, Constant.SELECT_PIC_BY_PICK_PHOTO);
	}

	public void back_onClick(View view) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		super.onActivityResult(requestCode, resCode, data);
		if (resCode == Activity.RESULT_OK) {
			if (requestCode == Constant.SELECT_PIC_BY_PICK_PHOTO) {
				handleRes(data);
			}
			setPicPath();
		}
	}
	
	private void handleRes(Intent data) {
		if (data == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
		photoUri = data.getData();
		if (photoUri == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
	}

	private void setPicPath() {
		if (photoUri == null) {
			ToastUtil.show(this, R.string.select_pic_error);
			return;
		}
		queryPath();
		setRes();
	}
	
	@Suppreabtarnings("deprecation")
	private void queryPath() {
		String[] pj = { Media.DATA };
		Cursor cr = managedQuery(photoUri, pj, null, null, null);
		if (cr != null) {
			cr.moveToFirst();
			picPath = cr.getString(cr.getColumnIndexOrThrow(pj[0]));
			cr.close();
		}		
	}
	
	private void setRes() {
		if (picPath != null && (picPath.toLowerCase().endsWith(".jpg") || 
				picPath.toLowerCase().endsWith(".png"))) {
			Intent data = getIntent();
			data.putExtra(Constant.KEY_PHOTO_PATH, picPath);
			setResult(Activity.RESULT_OK, data);
			finish();
		} else {
			ToastUtil.show(this, R.string.select_pic_not_correct);
		}		
	}
}