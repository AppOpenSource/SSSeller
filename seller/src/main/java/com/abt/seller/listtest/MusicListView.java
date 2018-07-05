package com.abt.seller.listtest;

import java.util.ArrayList;
import java.util.List;
import com.abt.seller.R;
import com.abt.seller.imgload.ImgLoad;
import com.abt.seller.utils.ListViewUtil;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MusicListView extends LinearLayout implements OnClickListener, OnItemClickListener{

	private LayoutInflater inflater;
	private Context context;
	private View view;
	private ListView listView;
	private ArrayList<String> imgList = new ArrayList<String>();
	private ArrayAdapter goodImgAdapter;	

	private static Handler handler;
	public static void setHandler(Handler handler) {
		MusicListView.handler = handler;
	}

	public MusicListView(Context context, ArrayList<String> st) {
		super(context);
		this.context = context;

		initView();
		initData();
	}

	public MusicListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initData();
	}

	public void freshData(){
		initData();
	}

	private void initView(){
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.audio_listview, this);
		listView = (ListView)view.findViewById(R.id.listview_audio_listview);
		//		adapter = new GoodImgAdapter(context, R.id.mediaList);
		goodImgAdapter = new GoodImgAdapter(context, R.layout.horizontal_list_item, imgList);
		//		listView.setDividerHeight(0);
		listView.setCacheColorHint(0);
		listView.setAdapter(goodImgAdapter);
		listView.setOnItemClickListener(this);
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

	private void initData() {
		String ss = "http://www.baidu.com/img/bdlogo.gif";
		String ss2 = "http://s.mall.360.cn/?eee=12hd_LOGO&fname=12hd_LOGO";
		imgList.add(ss);
		imgList.add(ss2);
		goodImgAdapter.notifyDataSetChanged();
		ListViewUtil.setListViewHeightBaseOnChildren(listView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//restart
		case 1:

			break;
			//back to mode
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//overload control

	}
}