package com.itheima.mobileguard.db.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * select location from data2 where area=(select outkey from data1 where id=?)
 */
public class NumberAddressDao {
		
	
		public static String query(String number,Context context){
			String address = number;
			//手机号码
			if(number.matches("^[1][3456789]\\d{9}$")){
				number = number.substring(0, 7);
				SQLiteDatabase db = 	SQLiteDatabase.openDatabase(
					new File(context.getFilesDir(),"address.db").getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
					Cursor cursor = db.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new	 String[]{number});
					if(cursor.moveToNext()){
						 address = cursor.getString(0);
					}
					db.close();
					cursor.close();
					return address;
			}else{
				switch (number.length()) {
				case 3:
					if("110".equals(number)){
						return "报警电话";
					}else if("120".equals(number)){
						return "急救电话";
					}else if("119".equals(number)){
						return "救火电话";
					}
					break;
				case 4:
					return "模拟器";
				case 5:
					return "客服电话";
				case 7:
					return "本地电话";
				case 8:
					return "本地电话";
				default:
					if(number.length()>=9 && number.startsWith("0")){
								SQLiteDatabase db = 	SQLiteDatabase.openDatabase(
								new File(context.getFilesDir(),"address.db").getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
								
								Cursor cursor = db.rawQuery("select location from data2 where area=?", new	 String[]{number.substring(1, 3)});
								if(cursor.moveToNext()){
									 address = cursor.getString(0);
									 address = address.substring(0,address.length()-2);
								}
								cursor.close();
								
								cursor = db.rawQuery("select location from data2 where area=?", new	 String[]{number.substring(1, 4)});
								if(cursor.moveToNext()){
									 address = cursor.getString(0);
									 address = address.substring(0,address.length()-2);
								}
								cursor.close();
								db.close();
					}
					break;
				}
			}
			return address;
		}
}
