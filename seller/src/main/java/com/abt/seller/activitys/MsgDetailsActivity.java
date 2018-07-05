package com.abt.seller.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.abt.seller.R;
import com.abt.seller.models.ChatMsgEntity;
import com.abt.seller.services.DBOpenHelper;
import com.abt.seller.services.DBService;
import com.abt.seller.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CutPasteId")
public class MsgDetailsActivity extends Activity {
	private static final int FROM_LOCAL = 0;
	private static final int FROM_OUTSIDE = 1;
	private static final int TYPE_1 = 0;
	private static final int TYPE_2 = 1;
	private static final int VIEW_TYPE = 2;
	private List<ChatMsgEntity> data;
	private DBOpenHelper helper;
	private ListView listview;
	private EditText editText;
	private MsgDetailsAdapter dAdapter;
	private DBService dbService;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_details);
		initDatas();
		initViews();
	}

	private void initViews() {
		dAdapter = new MsgDetailsAdapter(this, 0, data);
		editText = (EditText) findViewById(R.id.body_edit);
		listview = (ListView) findViewById(R.id.list_view);
		listview.setAdapter(dAdapter);
	}

	private void initDatas() {
		dbService = new DBService(this);
		helper = new DBOpenHelper(this);
		title = getIntent().getStringExtra("title");
		data = getDetailListData(title);
	}

	public void back_onClick(View view) {
		finish();
	}

	public void send_onClick(View view) {
		sendReply();
	}

	private void sendReply() {
		String reply = editText.getText().toString();
		if (reply.length() > 0) {
			ChatMsgEntity en = new ChatMsgEntity();
			en.setTime(DateUtil.getCurDate());
			en.setTitle(data.get(0).getTitle());
			en.setType(0);
			en.setMsg(reply);
			dbService.insertEntity(en);
			data = getDetailListData(title);
			dAdapter.notifyDataSetChanged();
			editText.setText("");
			listview.setSelection(listview.getCount() - 1);
		}
	}

	private List<ChatMsgEntity> getDetailListData(String title) {
		List<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		try {
			SQLiteDatabase db = helper.getWritableDatabase();
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT id, title, msg, time, type");
			sb.append("FROM msglist");
			sb.append("WHERE title = ?");
			Cursor cr = db.rawQuery(sb.toString(), new String[] { title });
			while (cr.moveToNext()) {
				ChatMsgEntity en = new ChatMsgEntity();
				en.setTitle(cr.getString(cr.getColumnIndex("title")));
				en.setMsg(cr.getString(cr.getColumnIndex("msg")));
				en.setTime(cr.getString(cr.getColumnIndex("time")));
				en.setType(cr.getInt(cr.getColumnIndex("type")));
				list.add(en);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private class ViewHolder1 {
		ImageView img;
		TextView title;
		TextView msg;
		TextView time;
	}

	private class ViewHolder2 {
		ImageView img;
		TextView title;
		TextView msg;
		TextView time;
	}

	private class MsgDetailsAdapter extends ArrayAdapter<ChatMsgEntity> {
		public MsgDetailsAdapter(Context cx, int res, List<ChatMsgEntity> objs) {
			super(cx, res, objs);
		}
		
		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			int isComMsg = getItem(position).getType();
			if (isComMsg == FROM_OUTSIDE) {
				return TYPE_1;
			} else if (isComMsg == FROM_LOCAL) {
				return TYPE_2;
			}
			return -1;
		}

		@Override
		public View getView(int position, View cv, ViewGroup parent) {
			ViewHolder1 holder1 = null;
			ViewHolder2 holder2 = null;
			int type = getItemViewType(position);
			if (cv == null) {
				inflateLayout(type, getContext(), cv, holder1, holder2);
			} else {
				getTags(type, cv, holder1, holder2);
			}
			refreshItem(type, cv, holder1, holder2, getItem(position));
			return cv;
		}
	}
	
	private void inflateLayout(int type, Context cx, View cv, ViewHolder1 holder1, ViewHolder2 holder2) {
		switch (type) {
		case TYPE_1:
			cv = LayoutInflater.from(cx).inflate(R.layout.msg_detail_listview, null);
			holder1 = new ViewHolder1();
			holder1.img = (ImageView) cv.findViewById(R.id.img);
			holder1.title = (TextView) cv.findViewById(R.id.title);
			holder1.msg = (TextView) cv.findViewById(R.id.msg);
			holder1.time = (TextView) cv.findViewById(R.id.time);
			cv.setTag(holder1);
			break;
		case TYPE_2:
			cv = LayoutInflater.from(cx).inflate(R.layout.msg_reply_listview, null);
			holder2 = new ViewHolder2();
			holder2.img = (ImageView) cv.findViewById(R.id.img);
			holder2.title = (TextView) cv.findViewById(R.id.title);
			holder2.msg = (TextView) cv.findViewById(R.id.msg);
			holder2.time = (TextView) cv.findViewById(R.id.time);
			cv.setTag(holder2);
			break;
		}
	}
	
	private void getTags(int type, View cv, ViewHolder1 holder1, ViewHolder2 holder2) {
		switch (type) {
		case TYPE_1:
			holder1 = (ViewHolder1) cv.getTag();
			break;
		case TYPE_2:
			holder2 = (ViewHolder2) cv.getTag();
			break;
		}
	}
	
	private void refreshItem(int type, View cv, ViewHolder1 holder1, ViewHolder2 holder2, ChatMsgEntity en) {
		switch (type) {
		case TYPE_1:
			holder1.img.setBackgroundResource((Integer) R.drawable.user_img);
			holder1.title.setText((String) en.getTitle());
			holder1.msg.setText((String) en.getMsg());
			holder1.time.setText((String) en.getTime());
			break;
		case TYPE_2:
			holder2.img.setBackgroundResource((Integer) R.drawable.mini_avatar_shadow);
			holder2.title.setText((String) en.getTitle());
			holder2.msg.setText((String) en.getMsg());
			holder2.time.setText((String) en.getTime());
			break;
		}
	}
}