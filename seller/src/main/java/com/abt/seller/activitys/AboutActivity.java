package com.abt.seller.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.abt.seller.R;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

	public void back_onClick(View view) {
		finish();
	}
}