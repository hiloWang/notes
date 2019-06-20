package com.itheima.mobileguard.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SmsUtils {
	/**
	 * 短信备份的回调函数，当执行短信备份时调用器其内部方法，
	 * 
	 * @author Administrator
	 * 
	 */
	public interface SmsBackupCallBack {
		/**
		 * 开始备份时调用 提供需要备份的总条数
		 * 
		 * @param size
		 *            短信的条数
		 */
		public void onStartBackup(int size);

		/**
		 * 备份过程中调用 提供当前进度
		 * 
		 * @param process
		 *            已备份的条数
		 */
		public void onBackuping(int process);
	}

	/**
	 * 短信备份可能比较耗时, 你应该在子线程中调用 ,内部对短信内容进行了加密，可以放心使用
	 * 
	 * @param context
	 *            上下文
	 * @param callback
	 *            备份回调
	 * @return 成功则返回true
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static boolean backupSms(Context context, SmsBackupCallBack callback)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// 备份之前先判断sd卡是否准备完毕，和判断sd卡的大小
		File sdCard = Environment.getExternalStorageDirectory();// sd卡的路径
		long usableSpace = sdCard.getFreeSpace();
		// sd卡可用并且空间大于1M 才可备份 否者跑出异常
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)
				&& usableSpace >= 1024l * 1024l) {
			// 利用内容提供者获取短信内容
			ContentResolver resolver = context.getContentResolver();
			// 定义uri
			Uri uri = Uri.parse("content://sms/");
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"body", "date", "type" }, null, null, null);
			// 获去短信的数量
			int count = cursor.getCount();
			callback.onStartBackup(count);

			// 利用xml序列化器 把段兴备份到xml文件中去
			XmlSerializer serializer = Xml.newSerializer();
			FileOutputStream fos = new FileOutputStream(new File(sdCard,
					"smsbackup.xml"));
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "smss");
			serializer.attribute(null, "size", String.valueOf(count));// 添加属性
																		// 短信的数量

			int process = 0;
			while (cursor.moveToNext()) {
				serializer.startTag(null, "sms");

				serializer.startTag(null, "address");
				serializer.text(cursor.getString(0));
				serializer.endTag(null, "address");

				// 加密短信
				serializer.startTag(null, "body");
				try {
					String cipherBody = Crypto.encrypt("zhantianyou12345",
							cursor.getString(1));
					serializer.text(cipherBody);
				} catch (Exception e) {
					e.printStackTrace();
					serializer.text("空短信");
				}
				serializer.endTag(null, "body");

				serializer.startTag(null, "date");
				serializer.text(cursor.getString(2));
				serializer.endTag(null, "date");
				serializer.startTag(null, "type");
				serializer.text(cursor.getString(3));
				serializer.endTag(null, "type");

				serializer.endTag(null, "sms");

				SystemClock.sleep(500);

				process++;
				callback.onBackuping(process);
			}
			serializer.endTag(null, "smss");
			serializer.endDocument();
			fos.close();
			cursor.close();
			return true;
		} else {
			throw new IllegalStateException("sd卡不可以或者空间不够");
		}
	}

	/**
	 * 短信备份的回调函数，当执行短信备份时调用器其内部方法，
	 * 
	 * @author Administrator
	 * 
	 */
	public interface SmsRestoreCallBack {
		/**
		 * 开始还原时调用 提供需要还原的总条数
		 * 
		 * @param size
		 *            短信的条数
		 */
		public void onStartRestore(int size);

		/**
		 * 还原过程中调用 提供当前进度
		 * 
		 * @param process
		 *            已还原的条数
		 */
		public void onRestoreing(int process);
	}

	/**
	 * 
	 * @param context
	 * @param callback
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public static boolean resotreSms(Context context,
			SmsRestoreCallBack callback) throws XmlPullParserException, IOException,IllegalStateException{
		// 判断 是否备份文件存在 读取sd卡的 文件
			File smsFile = new File(Environment.getExternalStorageDirectory(),"smsbackup.xml");
			if(smsFile.exists()){
				// 解析xml文件。
				// 1. 创建pull解析器
				XmlPullParser parser = Xml.newPullParser();
				// 2.初始化pull解析器，设置编码 inputstream
				FileInputStream fis =  new FileInputStream(smsFile);
				parser.setInput(fis, "utf-8");
				int eventType = parser.getEventType();
				// 3.解析xml文件 while(文档末尾）
				// {
				// 读取属性 size 总个数据. 调用接口的方法 beforeSmsRestore
				// 每读取到一条短信 就把这个短信 body（解密） address date type获取出来
				// 利用内容提供者 resolver.insert(Uri.parse("content://sms/"),contentValue);
				// 每还原条 count++ 调用onSmsRestore(count);
				// }
				//得到内容解析器
				ContentResolver resolver = context.getContentResolver();
				Uri uri = Uri.parse("content://sms/");
				String address = null;
				String body = null;
				String date = null;
				String type = null;
				ContentValues values = null;
				int count = 0;
				while(eventType != XmlPullParser.END_DOCUMENT){//没有解析到结尾就不停止
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if("smss".equals(parser.getName())){
							String size = parser.getAttributeValue(null, "size");
							callback.onStartRestore(Integer.parseInt(size));
						}else if("sms".equals(parser.getName())){
							
						}else if("address".equals(parser.getName())){
							address = parser.nextText();
						}else if("body".equals(parser.getName())){
							body = parser.nextText();
						}else if("date".equals(parser.getName())){
							date = parser.nextText();
						}else if("type".equals(parser.getName())){
							type = parser.nextText();
						}
						break;

					case XmlPullParser.END_TAG:
						 if("sms".equals(parser.getName())){
								 values = new ContentValues();
								 values.put("address", address);
								 
								 if("空短信".equals(body)){
									 values.put("body", "");
								 }else{
									 try {
										values.put("body", Crypto.decrypt("zhantianyou12345", body));
									} catch (Exception e) {
										 values.put("body", "");
										e.printStackTrace();
									}
								 }
								
								 values.put("date", date);
								 values.put("type", type);
								 resolver.insert(uri, values);
								 count ++;
								 callback.onRestoreing(count);
							}
						break;
					}
					eventType = parser.next();
				}
				return true;
			}else{
				throw new IllegalStateException("文件没有找到");
			}
	}
	
	public  static void deleteSms(Context context){
		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(uri, null, null);
	}
}
