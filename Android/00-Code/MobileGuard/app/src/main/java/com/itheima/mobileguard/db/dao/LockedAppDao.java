package com.itheima.mobileguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobileguard.db.AppLockSQLiteOpenHelper;

public class LockedAppDao {
	//数据库帮助类
	private AppLockSQLiteOpenHelper helper;

	public LockedAppDao(Context context) {
		helper = new AppLockSQLiteOpenHelper(context);
	}
	/**
	 * 查询某个APP是否已经加锁了
	 * @return
	 */
	public boolean isLocked(String packagename){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id from infos where packagename=?", new String[]{packagename});
		int count = cursor.getCount();
		db.close();
		cursor.close();
		if(count != 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 查询所有加锁了的app
	 * @return
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> names = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select packagename from infos",null);
		while(cursor.moveToNext()){
			names.add(cursor.getString(0));
		}
		db.close();
		cursor.close();
		return names;
	}
	
	/**
	 * 添加一条加锁的APP信息
	 * @param packagename
	 * @return
	 */
	public boolean add(String packagename){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new  ContentValues();
		values.put("packagename", packagename);
		long result = 	db.insert("infos", null, values);
		db.close();
		if(result != 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 程序解除锁定 删除信息
	 */
	public boolean delete(String packagename){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new  ContentValues();
		values.put("packagename", packagename);
		int result = db.delete("infos", "packagename=?", new String[]{packagename});
		db.close();
		if(result != 0){
			return true;
		}else{
			return false;
		}
	}
}
