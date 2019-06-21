package com.itheima.mobileguard.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.domain.ContactInfo;
import com.itheima.mobileguard.engine.ContactInfoParser;

public class ListContactActivity extends Activity {
	private ListView lv_listcontact;
	private List<ContactInfo> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listcontact);
		lv_listcontact = (ListView) findViewById(R.id.lv_listcontact);

		contacts = ContactInfoParser.findAll(this);
		lv_listcontact.setAdapter(new ContactAdapter());

		lv_listcontact.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("phone", contacts.get(position).getPhone());
				setResult(1, intent);
				finish();
			}
		});
	}

	private class ContactAdapter extends BaseAdapter {
		public int getCount() {
			return contacts.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(ListContactActivity.this,
						R.layout.item_listcontact, null);
			} else {
				view = convertView;
			}

			TextView tv_name = (TextView) view
					.findViewById(R.id.tv_contactname);
			TextView tv_number = (TextView) view
					.findViewById(R.id.tv_contactnumber);
			String name = contacts.get(position).getName();
			String phone = contacts.get(position).getPhone();
			tv_name.setText("ÐÕÃû£º"+name);
			tv_number.setText("ºÅÂë£º"+phone);
			if (name == null || "".equals(name)) {
				tv_name.setText("ÐÕÃû£º[¿Õ]");
			}
			if(phone == null ||"".equals( phone)){
				tv_number.setText("ºÅÂë£º[¿Õ]");
			}
			return view;
		}

	}
}
