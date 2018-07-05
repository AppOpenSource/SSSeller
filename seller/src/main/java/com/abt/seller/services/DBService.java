package com.abt.seller.services;

import com.abt.seller.models.ChatMsgEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private DBOpenHelper dbHelper;
	private SQLiteDatabase db;

	public DBService(Context context) {
		dbHelper = new DBOpenHelper(context);
	}

	public void insertEntity(ChatMsgEntity entity) {
		db = dbHelper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("title", entity.getTitle());
			cv.put("time", entity.getTime());
			cv.put("msg", entity.getMsg());
			cv.put("type", entity.getType());
			cv.put("img", 0);
			db.insert("msglist", null, cv);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public void updateEntity(ChatMsgEntity entity) throws Exception {
		db = dbHelper.getWritableDatabase();
		try {
			ContentValues cv = new ContentValues();
			cv.put("id", entity.getId());
			cv.put("title", entity.getTitle());
			cv.put("time", entity.getTime());
			cv.put("msg", entity.getMsg());
			cv.put("type", entity.getType());
			db.update("msglist", cv, "_id = ?", new String[] { entity.getId()
					+ "" });
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public ChatMsgEntity getEntityById(int id) throws Exception {
		if (id < 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT id, title, time,");
		sb.append("msg, type FROM updates ");
		sb.append("WHERE id = ?");
		ChatMsgEntity cme = new ChatMsgEntity();
		db = dbHelper.getWritableDatabase();
		Cursor cs = null;
		try {
			cs = db.rawQuery(sb.toString(), new String[] { id + "" });
			while (cs.moveToNext()) {
				cme.setId(cs.getInt(cs.getColumnIndex("_id")));
				cme.setTitle((cs.getString(cs.getColumnIndex("title"))));
				cme.setTime((cs.getString(cs.getColumnIndex("time"))));
				cme.setMsg((cs.getString(cs.getColumnIndex("msg"))));
				cme.setType(cs.getInt((cs.getColumnIndex("type"))));
			}
		} finally {
			if (cs != null) {
				cs.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return cme;
	}
}