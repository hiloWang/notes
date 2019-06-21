package com.itheima.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 应用打开数据库的帮助类
 * @author Administrator
 *
 */
public class CallSafeSQLiteOpenHelper extends SQLiteOpenHelper {

	public CallSafeSQLiteOpenHelper(Context context) {
		super(context, "callsafe.db", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		
		 // 创建黑名单表格
			db.execSQL("create table blackinfo(_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
