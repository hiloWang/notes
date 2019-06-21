package com.mwqi.http.protocol;

import android.util.Log;
import com.mwqi.bean.CategoryInfo;
import com.mwqi.utils.LogUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mwqi on 2014/6/8.
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfo>> {
	@Override
	protected String getKey() {
		return "category";
	}

	// 解析json
	@Override
	protected List<CategoryInfo> parseFromJson(String json) {
		try {
			List<CategoryInfo> list = new ArrayList<CategoryInfo>();
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);

				String title = obj.getString("title");
				CategoryInfo info = new CategoryInfo();
				info.setTitle(true);
				info.setTitle(title);
				list.add(info);

				JSONArray infos = obj.getJSONArray("infos");
				for (int j = 0; j < infos.length(); j++) {
					JSONObject category = infos.optJSONObject(j);
					info = new CategoryInfo();
					info.setImageUrl1(category.optString("url1"));
					info.setImageUrl2(category.optString("url2"));
					info.setImageUrl3(category.optString("url3"));
					info.setName1(category.optString("name1"));
					info.setName2(category.optString("name2"));
					info.setName3(category.optString("name3"));
					info.setTitle(false);
					info.setTitle(title);
					list.add(info);
				}
			}
			return list;
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return null;
	}
}
