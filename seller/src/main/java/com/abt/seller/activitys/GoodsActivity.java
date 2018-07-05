package com.abt.seller.activitys;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.imgload.ImgLoad;
import com.abt.seller.models.Good;
import com.abt.seller.models.Type;
import com.abt.seller.services.GoodsListService;
import com.abt.seller.services.GoodsTypeAddService;
import com.abt.seller.services.GoodsTypeService;
import com.abt.seller.utils.ToastUtil;
import com.abt.seller.views.PullRefreshView;
import com.abt.seller.views.PullRefreshView.OnFooterRefreshListener;
import com.abt.seller.views.PullRefreshView.OnHeaderRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodsActivity extends ListActivity implements OnItemClickListener,
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private static final int LOAD_DATA = 0;
	private static final int NORMAL = 1;
	private static final int HEAD = 2;
	private static final int FOOT = 3;
	private static final int LOAD_TYPE = 4;
	private static final int ADD_TYPE = 5;
	private static final int LOAD_STATUS = 6;
	private static final int GOOD_STATUS_UP = 1;
	private static final int GOOD_STATUS_DOWN = 2;
	private static int CURRENT_GOOD_STATUS = GOOD_STATUS_UP; // 默认订单状态 已上架
	private static final int CATAALL = 0; // 商品品类"全部"
	private static int CURRENT_GOOD_CATA = 0; //当前商品品类id
	private static String KEYWORD = ""; //当前商品搜索初值
	private ListView leftListview;
	private ListView listView;
	private Dialog proDialog;
	private GoodsAdapter goodsAdapter;
	private LeftAdapter leftAdapter;
	private PullRefreshView pullRefreshView;
	private List<Type> lefts = new ArrayList<Type>();
	private List<Good> goods = new ArrayList<Good>();
	private String newType;
	private int typePositon = 0;
	private int shopsid = 1;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_DATA:
				refreshUI(msg);
				break;
			case LOAD_TYPE:
				refreshTypeList((List<Type>) msg.obj);
				break;
			case ADD_TYPE:
				addType(msg);
				break;
			case LOAD_STATUS:
				statusFilter(msg);
				break;
			}
		}
	};
	
	private void addType(Message msg) {
		addToList(msg.arg1, (List<Type>)msg.obj);
	}
	
	private void statusFilter(Message msg) {
		List<Good> gds = (List<Good>) msg.obj;
		if (gds != null && !gds.isEmpty()) {
			goods.clear();
			goods.addAll(gds);
			goodsAdapter.notifyDataSetChanged();
		} else {
			goods.clear();
			goodsAdapter.notifyDataSetChanged();
			ToastUtil.show(this, "没有要显示的数据");
		}
		
		if (msg.arg2 == HEAD) {
			pullRefreshView.onHeaderRefreshComplete();
		} else if (msg.arg2 == FOOT) {
			pullRefreshView.onFooterRefreshComplete();
		}
		proDialog.dismiss();
	}
	
	@SuppressWarnings("unchecked")
	private void refreshUI(Message msg) {
		refreshList((List<Good>) msg.obj);
		if (msg.arg1 == HEAD) {
			pullRefreshView.onHeaderRefreshComplete();
		} else if (msg.arg1 == FOOT) {
			pullRefreshView.onFooterRefreshComplete();
		}
		proDialog.dismiss();
	}
	
	private void refreshList(List<Good> gds) {
		if (gds != null && !gds.isEmpty()) {
			goods.clear();
			goods.addAll(gds);
			goodsAdapter.notifyDataSetChanged();
		} else {
			goods.clear();
			goodsAdapter.notifyDataSetChanged();
			ToastUtil.show(this, "没有要显示的数据");
		}		
	}
	
	private void refreshTypeList(List<Type> gds) {
		if (gds != null && !gds.isEmpty()) {
			lefts.clear();
			lefts.addAll(gds);
			lefts.add(new Type(0, 0, "新增品类", 0, "", 0, 1));
			lefts.get(typePositon).setState(1);
			leftAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.show(this, "没有要显示的商品品类");
		}		
	}
	
	private void addToList(int added, List<Type> list) {
		if (added == 0) {
			lefts.remove(lefts.size() -1);
			lefts.add(new Type(list.get(0).getId(), list.get(0).getShopsid(), 
					list.get(0).getName(), list.get(0).getPid(), 
					list.get(0).getPath(), 0, 0));
			lefts.add(new Type(0, 0, "新增品类", 0, "", 0, 1));
		} else {
			ToastUtil.show(this, "新增品类失败");
		}
		proDialog.dismiss();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		initViews();
	}

	private void initViews() {
		showDialog();
		
		initGoodsListView();
		
		initSpinner();
        
        initLeftListView();
		
        initSearchViews();
	}

	private void initGoodsListView() {
		pullRefreshView = (PullRefreshView) findViewById(R.id.pull_refresh_view);
		pullRefreshView.setOnHeaderRefreshListener(this);
		pullRefreshView.setOnFooterRefreshListener(this);
		goodsAdapter = new GoodsAdapter(this, R.layout.good_item, goods);
		listView = getListView();
		listView.setAdapter(goodsAdapter);
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(false);
		listView.setOnItemClickListener(this);
	}

	private void initSpinner() {
		Spinner sp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.upndown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showDialog();
				final int status = arg2 + 1; // 1 已上架 , 2已下架
				CURRENT_GOOD_STATUS = status;
				new Thread(new Runnable() {
					@Override
					public void run() {
						getGoodsByStatus(NORMAL); // 状态, 位置
					}
				}).start();	 
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
        });
	}

	private void initLeftListView() {
//		lefts.add(new Category(1, "全部", 0));
//		lefts.add(new Category(0, "食品", 0));
//		lefts.add(new Category(0, "饮料", 0));
//		lefts.add(new Category(0, "果汁", 0));
		lefts.add(new Type(0, 0, "新增品类", 0, "", 0, 1));
		leftAdapter = new LeftAdapter(this, R.layout.good_left_item, lefts);
		leftListview = (ListView) findViewById(R.id.listview_left);
		leftListview.setAdapter(leftAdapter);
		leftListview.setDividerHeight(0);
		leftListview.setCacheColorHint(0);
		leftListview.setDrawingCacheEnabled(false);
		leftListview.setVerticalScrollBarEnabled(false);
		leftListview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(android.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				typePositon = arg2;
				if (arg2 == (lefts.size() - 1)) { // 新增品类被点击
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.good_add_dialog, (ViewGroup) findViewById(R.id.dialog));
					TextView btn = (TextView) layout.findViewById(R.id.ok);
					final EditText etname = (EditText) layout.findViewById(R.id.etname);
					final Dialog dialog = new Dialog(GoodsActivity.this, R.style.dialog);
					dialog.setContentView(layout);
					dialog.show();
					btn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							showDialog();
							newType = etname.getText().toString();
							if (!newType.equalsIgnoreCase("")) {
								addTypes(newType);
							}
							dialog.dismiss();
						}
					});
					return;
				} else {
					showDialog();
					CURRENT_GOOD_CATA = lefts.get(arg2).getId();
					new Thread(new Runnable() {
						@Override
						public void run() {
							getGoodsByStatus(NORMAL);
						}
					}).start();
				}
				
				for (int i = 0; i < lefts.size(); i++) {
					if (arg2 == i) { // 点击的条目
						if (lefts.get(i).getState() == 0) lefts.get(i).setState(1); // 被选中
					} else {	// 其他条目
						if (lefts.get(i).getState() == 1) lefts.get(i).setState(0); // 注销选中
					}
				}
				leftAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void initSearchViews() {
		final EditText inputET = (EditText) findViewById(R.id.input_et);
		Button search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				KEYWORD = inputET.getText().toString().trim();
				showDialog();
				new Thread(new Runnable() {
					@Override
					public void run() {
						getGoodsByStatus(NORMAL);
					}
				}).start();
			}
		});
	}
	
	private void showDialog() {
		if (proDialog == null) {
			proDialog = new Dialog(GoodsActivity.this, R.style.theme_dialog_alert);
			proDialog.setContentView(R.layout.window_layout);
		}
		if (!proDialog.isShowing()) {
			proDialog.show();
		}
	}

	public void add_onClick(View view) {
		Intent intent = new Intent(this, GoodAddActivity.class);
		intent.putExtra("lefts", (Serializable)lefts);
		intent.putExtra("shopsid", shopsid);
		intent.putExtra("typeid", lefts.get(typePositon + 1).getId());
		startActivity(intent);
	}
	
	private class LeftAdapter extends ArrayAdapter<Type> {
		public LeftAdapter(Context cx, int resId, List<Type> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolderLeft holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.good_left_item, null);
				holder = new ViewHolderLeft();
				holder.item = (TextView) cv.findViewById(R.id.item);
				cv.setTag(holder);
			} else {
				holder = (ViewHolderLeft) cv.getTag();
			}

			holder.item.setText(getItem(position).getName());
			
			if (getItem(position).getType() == 1) {
				holder.item.setBackgroundResource(R.drawable.goods_add_btn_bg);
			} else {
				if (getItem(position).getState() == 1) {
					holder.item.setBackgroundResource(R.drawable.left_press);
				} else {
					holder.item.setBackgroundResource(R.drawable.goods_left_bg);
				}
			}
			return cv;
		}
	}

	private class GoodsAdapter extends ArrayAdapter<Good> {
		public GoodsAdapter(Context cx, int resId, List<Good> objs) {
			super(cx, resId, objs);
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder holder;
			if (cv == null) {
				cv = LayoutInflater.from(getContext()).inflate(R.layout.good_item, null);
				holder = new ViewHolder();
				findViews(holder, cv);
				cv.setTag(holder);
			} else {
				holder = (ViewHolder) cv.getTag();
			}
			refreshItem(holder, getItem(position), ImgLoad.getInstance());
			return cv;
		}
	}
	
	private void findViews(ViewHolder holder, View cv) {
		holder.bg = (ImageView) cv.findViewById(R.id.background);
		holder.icon = (ImageView) cv.findViewById(R.id.icon);
		holder.goodId = (TextView) cv.findViewById(R.id.product_no);
		holder.goodName = (TextView) cv.findViewById(R.id.name);
		holder.goodPrice = (TextView) cv.findViewById(R.id.price);
	}
	
	private void refreshItem(ViewHolder holder, Good good, ImgLoad loader) {
		if (good.getState() == 1) {
			holder.bg.setBackgroundResource(R.drawable.yishangjia);
		} else if (good.getState() == 2) { 
			holder.bg.setBackgroundResource(R.drawable.yixiajia);
		}

		if (good.getPicpath() != null) {
			holder.icon.setTag(good.getPicpath());
			loader.addTask(good.getPicpath(), holder.icon);
			loader.doTask();
		} else {
			holder.icon.setImageResource(R.drawable.ic_launcher);
		}
		
		holder.goodId.setText(good.getGoodsnum() + "");
		holder.goodPrice.setText(good.getGoodsprice() + "");
		Log.d("TAG", "goods activity price : "  + good.getGoodsprice());
		holder.goodName.setText(good.getGoods());
	}

	private class ViewHolder {
		ImageView bg;
		ImageView icon;
		TextView goodId;
		TextView goodName;
		TextView goodPrice;
	}
	
	private class ViewHolderLeft {
		TextView item;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent in = new Intent(this, GoodDetailsActivity.class);
		in.putExtra("good", goods.get(arg2));
		goods.get(0).getImgs();
		in.putExtra("position", typePositon);
		startActivity(in);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initDatas(NORMAL);
		shopsid = getIntent().getIntExtra("shopsid", 1);
	}
	
	@Override
	public void onHeaderRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		initDatas(HEAD);
	}

	@Override
	public void onFooterRefresh(PullRefreshView view) {
		// TODO Auto-generated method stub
		initDatas(FOOT);
	}

	private void initDatas(final int position) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getGoodsByStatus(position);
				getTypes();
			}
		}).start();
	}
	
	private void getGoodsByStatus(int position) { // type {goods types}
		GoodsListService gls = new GoodsListService(this);
		Message msg = handler.obtainMessage(LOAD_STATUS);
		msg.obj = gls.getGoodsByStatus(CURRENT_GOOD_STATUS, CURRENT_GOOD_CATA, KEYWORD);
		msg.arg1 = CURRENT_GOOD_STATUS;
		msg.arg2 = position;
		msg.sendToTarget();		
	}
	
	private void getTypes() { // get goods types
		GoodsTypeService gts = new GoodsTypeService(this);
		Message msg = handler.obtainMessage(LOAD_TYPE);
		msg.obj = gts.getTypes();
		msg.sendToTarget();		
	}
	
	private void addTypes(String type) {
		GoodsTypeAddService gts = new GoodsTypeAddService(this);
		Message msg = handler.obtainMessage(ADD_TYPE);
		msg.obj = gts.addType(type);
		if (((List<Type>)msg.obj).size() > 0) {
			msg.arg1 = 0; // add type success
		} else {
			msg.arg1 = 1; // failed
		}
		msg.sendToTarget();
	}
}