package com.itheima.mobileguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.itheima.mobileguard.db.CallSafeSQLiteOpenHelper;
import com.itheima.mobileguard.domain.BlackInfo;

/**
 * 实现对黑名单表格的curd
 * @author Administrator
 *
 */
public class BlackNumberDao {

		private CallSafeSQLiteOpenHelper helper;
		/**
		 * 初始化helper类
		 * @param context
		 */
		public BlackNumberDao(Context context) {
			helper = new CallSafeSQLiteOpenHelper(context);
		}
		/**
		 * 添加一条黑名单
		 * @param number 黑名单号码
		 * @param mode 模式
		 * @return 添加成功返回 true 否则为false
		 */
		public boolean insert(String number,String mode){
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values  = new ContentValues();
			values.put("number", number);
			values.put("mode", mode);
			long result = db.insert("blackinfo", null, values);
			db.close();
			if(result == -1){
				return false;
			}else{
				return true;
			}
		}
		/**
		 * 更改黑名单号码模式
		 * @param number 号码
		 * @param mode 模式
		 * @return 添加成功返回 true 否则为false
		 */
		public boolean update(String number,String mode){
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values  = new ContentValues();
			values.put("mode", mode);
			int result = db.update("blackinfo", values, "number=?", new String[]{number});
			db.close();
			if(result == 0){
				return false;
			}
			else{
				return true;
			}
		}
		/**
		 * 删除黑名单号码
		 * @param number 号码
		 * @return 添加成功返回 true 否则为false
		 */
		public boolean delete(String number){
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues values  = new ContentValues();
			int result = db.delete("blackinfo", "number=?", new String[]{number});
			db.close();
			if(result == 0){
				return false;
			}
			else{
				return true;
			}
		}
		
		/**
		 * 查找出所有的黑名单信息
		 * @return 所有的黑名单信息
		 */
		public List<BlackInfo> findAll(){
			SQLiteDatabase db  = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select number,mode from blackinfo", null);
			List<BlackInfo> infos = new ArrayList<BlackInfo>();
			while(cursor.moveToNext()	){
				String number = cursor.getString(0);
				String mode = cursor.getString(1);
				infos.add(new BlackInfo(number,mode ));
			}
			cursor.close();
			db.close();
			SystemClock.sleep(3000);
			return infos;
		}
		
		/**
		 * 
		 * @param pageNumber 查询的页码 从零开始
		 * @param pageSize 	每页显示的数量
		 * @return
		 */
		public List<BlackInfo> findpart(int pageNumber,int pageSize){
			SQLiteDatabase db  = helper.getReadableDatabase();
			// 查询多少  偏移多少
			Cursor cursor = db.rawQuery("select number,mode from blackinfo limit ? offset ? ", new String[]{
					String.valueOf(pageSize),String.valueOf(pageNumber*pageSize)
			});
			List<BlackInfo> infos = new ArrayList<BlackInfo>();
			while(cursor.moveToNext()	){
				String number = cursor.getString(0);
				String mode = cursor.getString(1);
				infos.add(new BlackInfo(number,mode ));
			}
			cursor.close();
			db.close();
			SystemClock.sleep(500);
			return infos;
		}
		/**
		 * 分批加载数据
		 * @param startIndex 查询的页码 从零开始
		 * @param maxSize 	每页显示的数量
		 * @return
		 */
		public List<BlackInfo> findpart2(int startIndex,int maxSize){
			SQLiteDatabase db  = helper.getReadableDatabase();
			// 查询多少  偏移多少 应当倒序 添加的数据才会显示在最前面 limit放在最后
			Cursor cursor = db.rawQuery("select number,mode from blackinfo order by _id desc limit ? offset ? ", new String[]{
					String.valueOf(maxSize),String.valueOf(startIndex)
			});
			List<BlackInfo> infos = new ArrayList<BlackInfo>();
			while(cursor.moveToNext()	){
				String number = cursor.getString(0);
				String mode = cursor.getString(1);
				infos.add(new BlackInfo(number,mode ));
			}
			cursor.close();
			db.close();
			return infos;
		}
		
		
		/**
		 * 根据号码查询出拦截模式
		 * @param number
		 * @return
		 */
		public String findModeByNumber(String number){
			SQLiteDatabase db  = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select mode from blackinfo where number=?", new String[]{number});
			String mode = "0";
			if(cursor.moveToNext()){
				mode = cursor.getString(0);
			}
			db.close();
			cursor.close();
			return mode;
		}
		
		/**
		 * 查询表格记录的数量
		 * @return
		 */
		public int getTotalRecord(){
			SQLiteDatabase db  = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select count(*) from blackinfo", null);
			int page = 0;
			if(cursor.moveToNext()){
				page = cursor.getInt(0);
			}
			return page;
		}
}













