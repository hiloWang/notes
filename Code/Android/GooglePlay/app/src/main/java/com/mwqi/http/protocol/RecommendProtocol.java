package com.mwqi.http.protocol;

import com.mwqi.utils.LogUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwqi on 2014/6/8.
 */
public class RecommendProtocol extends BaseProtocol<List<String>> {
	@Override
	protected String getKey() {
		return "recommend";
	}

	@Override
	protected List<String> parseFromJson(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				String str = array.optString(i);
				list.add(str);
			}
			return list;
		} catch (Exception e) {
			LogUtils.e(e);
			return null;
		}
	}
}
