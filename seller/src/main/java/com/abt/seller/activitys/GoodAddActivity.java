package com.abt.seller.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Good;
import com.abt.seller.models.Type;
import com.abt.seller.services.GoodDetailService;
import com.abt.seller.utils.ImageUtil;
import com.abt.seller.utils.ToastUtil;
import com.abt.seller.utils.UploadUtil;
import com.abt.seller.utils.UploadUtil.OnUploadProcessListener;
import com.abt.seller.views.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class GoodAddActivity extends Activity implements OnUploadProcessListener {
	protected static final int REFRESH_UI = 0;
	protected static final int REFRESH_UI_ON_SALE = 1;
	protected static final int REFRESH_UI_SAVE_OK = 2;
	protected static final int REFRESH_UI_DEL = 3;
	protected static final int TO_UPLOAD_FILE = 4;
	protected static final int UPLOAD_FILE_DONE = 5;
	protected static final int TO_SELECT_PHOTO = 6;
	protected static final int UPLOAD_INIT_PROCESS = 7;
	protected static final int UPLOAD_IN_PROCESS = 8;
	protected static final int REFRESH_UI_SAVE_FAIL = 9;
	private TextView detailName;
	private TextView detailBrand;
	private TextView detailCata;
	private TextView detailPrice;
	private TextView detailContext;
	private TextView detailNum;
	private Good good;
	private GoodImgAdapter goodImgAdapter;
	private HorizontalListView goodImgList;
	private ProgressDialog proDialog;
	private String picPath;
	private ArrayList<String> imgList = new ArrayList<String>();
	private Spinner detailCataSpinner;
	private SpinnerAdapter spinnerAdapter;
	private List<String> types = new ArrayList<String>();
	private static int shopsid = 0;
	private static int typeid = 0;
	private List<Type> typeList;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_UI:
				if (good != null) refreshUI();
				break;
			case REFRESH_UI_SAVE_OK:
				proDialog.dismiss();
				showToast(R.string.save_success);
				break;
			case REFRESH_UI_SAVE_FAIL:
				proDialog.dismiss();
				showToast(R.string.save_failed);
				break;
			case REFRESH_UI_DEL:
				showToast(R.string.dele_success);
				break;
			}
		}
	};
	
	private void showToast(int id) {
		ToastUtil.show(this, id);
	}

	private void refreshUI() {
		imgList.clear();
//		imgList.addAll(good.getImgs());
		goodImgAdapter.notifyDataSetChanged();
		detailPrice.setText(good.getMarketprice() + "");
		if (good.getGoods() != null)	detailName.setText(good.getGoods());
//		if (good.getBrand() != null) detailBrand.setText(good.getBrand());
//		if (good.getCata() != null)	detailCata.setText(good.getCata());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_add);
		initGoodDetailViews();
		initGoodImgView();
		initSpinner();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		shopsid = getIntent().getIntExtra("shopsid", 1);
//		typeid = getIntent().getIntExtra("typeid", 1);
		initSpinnerData();
	}
	
	private void initSpinnerData() {
		typeList = (List<Type>) getIntent().getSerializableExtra("lefts");
		if (typeList.size() > 2) {
			typeList.remove(0);
			typeList.remove(typeList.size() - 1);
		}
		for (Type type : typeList) {
			types.add(type.getName());
		}
		spinnerAdapter.notifyDataSetChanged();		
	}
	
	private void initSpinner() {
		detailCataSpinner = (Spinner) findViewById(R.id.product_cata_spinner);
        spinnerAdapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        detailCataSpinner.setAdapter(spinnerAdapter);
        detailCataSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				detailCataId = typeList.get(arg2).getId();
				typeid = typeList.get(arg2).getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
        });
	}
	
	private class SpinnerAdapter extends ArrayAdapter<String> {
		public SpinnerAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
	}

	private void initGoodDetailViews() {
		proDialog = new ProgressDialog(this);
		detailName = (TextView) findViewById(R.id.product_detail_name);
		detailBrand = (TextView) findViewById(R.id.product_detail_brand);
		detailCata = (TextView) findViewById(R.id.product_detail_cata);
		detailPrice = (TextView) findViewById(R.id.product_detail_price);
		detailContext = (TextView) findViewById(R.id.good_details_content);
		detailNum = (TextView) findViewById(R.id.product_detail_num);
	} 
	
	public void initGoodImgView() {
		goodImgAdapter = new GoodImgAdapter(this, R.layout.horizontal_list_item, imgList);
		goodImgList = (HorizontalListView) findViewById(R.id.img_listview);
		goodImgList.setAdapter(goodImgAdapter);
		goodImgList.setOnItemClickListener(imgClickListener);
	}

	public void back_onClick(View view) {
		finish();
	}

	public void camera_onClick(View view) {
		Intent in = new Intent(this, SelectPicActivity.class);
		startActivityForResult(in, TO_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resCode, Intent data) {
		super.onActivityResult(requestCode, resCode, data);
		if (resCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);
			imgList.add(picPath);
			goodImgAdapter.notifyDataSetChanged();
		}
	}

	public void save_onClick(View view) {
		proDialog.setMessage(getResources().getString(R.string.updating_data));
		proDialog.show();
		
		if (imgList.isEmpty()) {
			proDialog.dismiss();
			showToast(R.string.plz_input_pic);
		} else {
			UploadUtil util = UploadUtil.getInstance(this);
			util.setOnUploadProcessListener(this);
			util.uploadImages(Constant.uploadImgURL, imgList);
		}
	}

	private class GoodImgAdapter extends ArrayAdapter<String> {
		public GoodImgAdapter(Context cx, int resId, List<String> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder = null;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.horizontal_list_item, null);
				holder = new ViewHolder();
				holder.img = (ImageView) cv.findViewById(R.id.img);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position));
			return cv;
		}
	}
	
	private void refreshItem(ViewHolder holder, String path) {
		Bitmap bmp = ImageUtil.getBitmap(path, 4);
		holder.img.setImageBitmap(bmp);		
	}
	
	private class ViewHolder {
		ImageView img;
	}

	private OnItemClickListener imgClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			startPagerActivity(position);
		}
	};

	private void startPagerActivity(int position) {
		Intent in = new Intent(this, ImgPagerActivity.class);
		String[] str = imgList.toArray(new String[imgList.size()]);
		in.putExtra("IMAGES", str);
		in.putExtra("IMAGE_TYPE", "LOCAL");
		in.putExtra("IMAGE_POSITION", position);
		startActivity(in);
	}

	@Override
	public void initUpload(int fileSize) {
		Message msg = handler.obtainMessage(UPLOAD_INIT_PROCESS);
		msg.arg1 = fileSize;
		msg.sendToTarget();
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		Message msg = handler.obtainMessage(UPLOAD_IN_PROCESS);
		msg.arg1 = uploadSize;
		msg.sendToTarget();
	}

	@Override
	public void onUploadDone(int responseCode, final String imageList) {
		if (responseCode == Constant.UPLOAD_SUCCESS_CODE) {
			saveGood(imageList);
		}
	}

	private void saveGood(final String imageList) {
		final String goodsName = detailName.getText().toString();
		final String brand = detailBrand.getText().toString();
		final String temp = detailPrice.getText().toString();
		final String content = detailContext.getText().toString();
		final String goodsnum = detailNum.getText().toString();
		final float price = TextUtils.isEmpty(temp) ? 0 : Float.parseFloat(temp);
		new Thread(new Runnable() {
			@Override
			public void run() {
//				save(name, brand, price, imageList);
				save(imageList, goodsName, shopsid, typeid,
						content, price, goodsnum);
			}
		}).start();
	}
	
	private void save(String image, String goodsName, int shopsid, int typeid,
			String content, float price, String goodsnum) {
		GoodDetailService gds = new GoodDetailService(this);
		if (gds.saveGood(image, goodsName, shopsid, typeid,
				content, price, goodsnum)) {
			handler.obtainMessage(REFRESH_UI_SAVE_OK).sendToTarget();
		} else {
			handler.obtainMessage(REFRESH_UI_SAVE_FAIL).sendToTarget();
		}	
	}
}