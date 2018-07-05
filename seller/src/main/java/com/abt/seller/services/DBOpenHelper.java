package com.abt.seller.services;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String NAME = "ssw.db";
	private static final int VERSION = 1;

	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS msglist(");
		sb.append("id integer primary key autoincrement,");
		sb.append("title varchar(32), msg varchar(1280),");
		sb.append("time varchar(32), img integer, type integer)");
		db.execSQL(sb.toString());
		initDatabase(db);
	}

	private void initDatabase(SQLiteDatabase db) {
		for (int i = 0; i < 10; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("insert into msglist(title, msg, time, type)");
			sb.append("values('小区物业', '最新消息', '09:00', '1')");
			db.execSQL(sb.toString());
		}
	}

	public boolean isTableExist(String tableName, SQLiteDatabase db) {
		if (tableName == null) {
			return false;
		}
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select count(*) as c from sqlite_master");
			sb.append("where type = 'table' and name = '");
			String sql = sb + tableName.trim() + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		db.execSQL("DROP TABLE IF EXISTS msglist");
		onCreate(db);
	}
}