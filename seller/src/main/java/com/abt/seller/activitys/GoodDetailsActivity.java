package com.abt.seller.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

import com.abt.seller.ImgPagerActivity;
import com.abt.seller.R;
import com.abt.seller.imgload.ImgLoad;
import com.abt.seller.models.Constant;
import com.abt.seller.models.Good;
import com.abt.seller.models.Type;
import com.abt.seller.services.GoodDetailService;
import com.abt.seller.utils.SystemUtil;
import com.abt.seller.utils.ToastUtil;
import com.abt.seller.views.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class GoodDetailsActivity extends Activity {
	protected static final int REFRESH_UI = 0;
	protected static final int REFRESH_UI_ON_SALE = 1;
	protected static final int REFRESH_UI_SAVE = 2;
	protected static final int REFRESH_UI_DEL = 3;
	protected static final int TO_UPLOAD_FILE = 4;
	protected static final int UPLOAD_FILE_DONE = 5;
	protected static final int TO_SELECT_PHOTO = 6;
	protected static final int UPLOAD_INIT_PROCESS = 7;
	protected static final int UPLOAD_IN_PROCESS = 8;
	private TextView detailName;
	private TextView detailContent;
	private TextView detailPrice;
	private TextView detailNum;
	private Spinner detailCataSpinner;
	private HorizontalListView listView;
	private GoodImgAdapter goodImgAdapter;
	private String picPath;
	private Good good;
	private int typePosition = 0;
	private Dialog proDialog;
	private ArrayList<String> imgList = new ArrayList<String>();
	private ArrayList<Type> typeList = new ArrayList<Type>();
	private List<String> types = new ArrayList<String>();
	private SpinnerAdapter spinnerAdapter;
	private int detailCataId = 0;
	private String picname = "";
	private int state = 1;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REFRESH_UI_ON_SALE:
				upOrDown(good.getState());
				break;
			case REFRESH_UI_SAVE:
				showToast("保存成功!");
				break;
			case REFRESH_UI_DEL:
				showToast("删除成功!");
				break;
			}
		}
	};
	
	private void showToast(String msg) {
		ToastUtil.show(this, msg);
	}
	
	private void upOrDown(int tag) {
		if (tag == 0) {
			ToastUtil.show(this, "已上架!");
		} else {
			ToastUtil.show(this, "已下架!");
		}	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_good_details);
		initViews();
	}

	private void initViews() {
		detailName = (TextView) findViewById(R.id.product_detail_name);
		detailContent = (TextView) findViewById(R.id.good_details_content);
		detailPrice = (TextView) findViewById(R.id.product_detail_price);
		detailNum = (TextView) findViewById(R.id.product_detail_num);
		
		initSpinner();
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
				detailCataId = typeList.get(arg2).getId();
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
	
	private void showDialog() {
		if (proDialog == null) {
			proDialog = new Dialog(GoodDetailsActivity.this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
		}
		if (!proDialog.isShowing()) {
			proDialog.show();
		}
	}
	
	public void back_onClick(View view) {
		finish();
	}

	public void camera_onClick(View view) {
//		 Intent intent = new Intent(this, SelectPicActivity.class);
//		 startActivityForResult(intent, TO_SELECT_PHOTO);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GoodDetailsAsyncTask().execute();
	}

	private class GoodDetailsAsyncTask extends AsyncTask<Void, Void, Boolean> {
		protected void onPreExecute() {
			preExecute();
		}

		protected Boolean doInBackground(Void... params) {
			return getDetail();
		}

		protected void onPostExecute(Boolean success) {
			initImgHolder();
		}
	}
	
	private void preExecute() {
		proDialog = new Dialog(this, R.style.theme_dialog_alert);
		proDialog.setContentView(R.layout.window_layout);
		proDialog.show();

		if (!SystemUtil.isNetworkConnected(this)) {
			proDialog.dismiss();
			ToastUtil.show(this, R.string.sorry_network_disconnected);
		}
	}
	
	private boolean getDetail() {
		good = (Good) getIntent().getSerializableExtra("good");
		typePosition = getIntent().getIntExtra("position", 0);
		if (good == null) return false;
		GoodDetailService gds = new GoodDetailService(this);
		good = gds.getGoodDetail(good.getId(), good.getShopsid());
		return true;		
	}

	public void initImgHolder() {
		goodImgAdapter = new GoodImgAdapter(this, R.layout.horizontal_list_item, imgList);
		listView = (HorizontalListView) findViewById(R.id.listview);
		listView.setAdapter(goodImgAdapter);
		listView.setOnItemClickListener(itemClickListener);
		
		if (good != null) refreshUI();
		proDialog.dismiss();
	}
	
	private void refreshUI() {
		imgList.clear();
		imgList.addAll(good.getImgs());
		goodImgAdapter.notifyDataSetChanged();
		if (good.getGoods() != null) detailName.setText(good.getGoods());
		detailContent.setText(good.getContent());
		detailPrice.setText(good.getGoodsprice() + "");
		Log.d("TAG", "good primitive price : " + good.getGoodsprice());
		detailNum.setText(good.getGoodsnum() + "");
		
		
        typeList = good.getTypes();
        for (Type type : typeList) {
        	types.add(type.getName());
        }
        detailCataSpinner.setSelection(typePosition - 1);
        spinnerAdapter.notifyDataSetChanged();
        
        state = good.getState();
		ArrayList<String> aa= (ArrayList<String>) good.getImgNames();
		if (aa.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("[\"");
			for (int i = 0; i < aa.size(); i++) {
				sb.append(aa.get(i));
				sb.append("\", \"");
			}
			sb.replace(sb.length() - 3, sb.length(), "");
			sb.append("]");
//			["52a2ea32d094b.png", "52a2ea32d14bc.png"]
			picname = sb.toString();
		}
	}

	public void save_onClick(View view) {
		updateGoodDetail();
	}

	private void updateGoodDetail() {
		final String goodsnum = detailNum.getText().toString();
		final String goods = detailName.getText().toString();
		final float goodsprice = Float.parseFloat(detailPrice.getText().toString());
		final String content = detailContent.getText().toString();

		Log.d("TAG", "assssssssssss picname : " + picname);
		new Thread(new Runnable() {
			@Override
			public void run() {
				saveGood(goodsnum, goods, goodsprice, content, picname);
			}
		}).start();
	}
	
	private void saveGood(String goodsnum, String goods, float goodsprice, 
			String content, String picname) {
		GoodDetailService gds = new GoodDetailService(this);
		if (gds.save(good.getId(), good.getShopsid(), detailCataId, goodsnum, 
				goods, goodsprice, content, picname, state)) {
			Message msg = handler.obtainMessage(REFRESH_UI_SAVE);
			msg.sendToTarget();
		}
	}
	
	public void down_onClick(View view) {
		state = 2; // 下架
		updateGoodDetail();
	}
	
	public void up_onClick(View view) {
		state = 1; // 上架
		updateGoodDetail();
	}
	
	private void setForSale(int isSale) {
		GoodDetailService gds = new GoodDetailService(this);
		if (gds.onSale(good.getId(), isSale)) {
			good.setState(isSale);
			Message msg = handler.obtainMessage(REFRESH_UI_ON_SALE);
			msg.sendToTarget();
		}
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

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int ps, long id) {
			startPagerActivity(ps);
		}
	};

	private void startPagerActivity(int position) {
		Intent in = new Intent(this, ImgPagerActivity.class);
		String[] str = imgList.toArray(new String[imgList.size()]);;
		in.putExtra("IMAGES", str);
		in.putExtra("IMAGE_POSITION", position);
		in.putExtra("IMAGE_TYPE", "REMOTE");
		startActivity(in);
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
			refreshItem(ImgLoad.getInstance(), getItem(position), holder);
			return cv;
		}
	}
	
	private void refreshItem(ImgLoad loader, String img, ViewHolder holder) {
		if (img != null) {
			holder.img.setTag(img);
			loader.addTask(img, holder.img);
			loader.doTask();
		}
	}
	
	private class ViewHolder {
		ImageView img;
	}
}