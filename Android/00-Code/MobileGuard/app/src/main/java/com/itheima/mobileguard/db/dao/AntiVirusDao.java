package com.itheima.mobileguard.db.dao;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {

	/**
	 * 判断病毒数据库是否存在
	 * @param context
	 * @return
	 */
	public static boolean isVirusDBExists(Context context) {
		File file = new File(context.getFilesDir(), "antivirus.db");
		return file.exists();
	}
	/**
	 * 获取病毒库的版本
	 * @param context
	 * @return 获取失败返回-1
	 */
	public static String  getVirusVersion(Context context){
		 SQLiteDatabase db = SQLiteDatabase.openDatabase( new File(context.getFilesDir(), "antivirus.db").getAbsolutePath()
				, null, SQLiteDatabase.OPEN_READONLY);
		 Cursor cursor = db.rawQuery("select subcnt from version", null);
		 String result = "0";
		 if(cursor.moveToNext()){//有结果 获取结果 
			 result =  cursor.getString(0);
		 }
		 db.close();
		 cursor.close();
		 return result;
	}
	/**
	 * 更新数据库版本
	 * @param context 上下文
	 * @param version 版本
	 * @return
	 */
	public static void updateVirusVersoin(Context context,String version){
		SQLiteDatabase db = SQLiteDatabase.openDatabase( new File(context.getFilesDir(), "antivirus.db").getAbsolutePath()
				, null, SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("subcnt", version);
		db.insert("version", null, values);
		db.close();
	}
	/**
	 * 更新数据库API
	 * @param context 上下文
	 * @param md5Code md5码
	 * @return
	 */
	public static void updateVirusAPI(Context context,String md5Code,String desc){
		SQLiteDatabase db = SQLiteDatabase.openDatabase( new File(context.getFilesDir(), "antivirus.db").getAbsolutePath()
				, null, SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("md5", md5Code);
		values.put("name", "xx神器");
		values.put("type",6);
		values.put("desc", desc);
		db.insert("datable", null, values);
		db.close();
	}
	
	/**
	 * 检查应用程序是否是病毒
	 * @param md5Code
	 * @return 是病毒返回病毒描述 否则返回null
	 */
	public static String check(Context context,String md5Code){
		 SQLiteDatabase db = SQLiteDatabase.openDatabase( new File(context.getFilesDir(), "antivirus.db").getAbsolutePath()
					, null, SQLiteDatabase.OPEN_READONLY);
			 Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5Code});
			 String desc = null;
			 if(cursor.moveToNext()){
				 desc = cursor.getString(0);
			 }
			 db.close();
			 cursor.close();
			 return desc;
	}
	
}
