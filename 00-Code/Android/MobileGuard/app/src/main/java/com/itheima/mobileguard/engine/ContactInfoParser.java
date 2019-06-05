package com.itheima.mobileguard.engine;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobileguard.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class ContactInfoParser {

	/**
	 * 获取系统全部联系人的API方法
	 * 
	 * @param context
	 * @return
	 */
	public static List<ContactInfo> findAll(Context context) {
		ContentResolver resolver = context.getContentResolver();
		// 1. 查询raw_contacts表，把联系人的id取出来
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			if (id != null) {
				System.out.println("联系人id：" + id);
				ContactInfo info = new ContactInfo();
				info.setId(id);
				// 2. 根据联系人的id，查询data表，把这个id的数据取出来
				// 系统api 查询data表的时候 不是真正的查询data表 而是查询的data表的视图
				Cursor dataCursor = resolver.query(datauri, new String[] {
						"data1", "mimetype" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						System.out.println("姓名=" + data1);
						info.setName(data1);
					} else if ("vnd.android.cursor.item/email_v2"
							.equals(mimetype)) {
						System.out.println("邮箱=" + data1);
						info.setEmail(data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						System.out.println("电话=" + data1);
						info.setPhone(data1);
					} else if ("vnd.android.cursor.item/im".equals(mimetype)) {
						System.out.println("QQ=" + data1);
						info.setQq(data1);
					}
				}
				//如果信息为空不应该显示在界面上
				if(info.getName() != null || info.getPhone() != null){
					infos.add(info);
				}
				System.out.println("------");
				dataCursor.close();
			}
		}
		cursor.close();
		return infos;
	}
}
