package me.ztiany.jsonanalyze.address;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import me.ztiany.jsonanalyze.AppContext;
import me.ztiany.jsonanalyze.utils.IOUtils;

/**
 * @author Ztiany
 * @version 1.0
 * Email: 1169654504@qq.com
 */
public class AddressInquirers {

    private static final String TAG = AddressInquirers.class.getSimpleName();

    private static final String CHINA_ADDRESS_PATH = "countryDivisions_cn_token.json";

    private final String mContent;

    public static final int PARSER_TYPE_GSON = 1;
    public static final int PARSER_TYPE_FASTJSON = 2;

    public AddressInquirers() {
        mContent = getAddressJson();
    }

    public long start(int parserType) {
        final long start = System.currentTimeMillis();
        List<AddressItem> items = parse(parserType, mContent);
        Log.d(TAG, "provinces count = " + items.size());
        long time = System.currentTimeMillis() - start;
        Log.d(TAG, getParserName(parserType) + " use time = " + time);
        return time;
    }

    private String getParserName(int parserType) {
        return parserType == PARSER_TYPE_GSON ? "GSON" : "FASTJSON";
    }

    private List<AddressItem> parse(int parserType, String s) {
        if (parserType == PARSER_TYPE_GSON) {
            return formGson(s);
        } else {
            return formFastJson(s);
        }
    }

    private String getAddressJson() {
        try {
            return IOUtils.convertToString(AppContext.getAppContext().getAssets().open(CHINA_ADDRESS_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private List<AddressItem> formGson(String content) {
        final Type type = new TypeToken<List<AddressItem>>() {
        }.getType();

        return new Gson().fromJson(content, type);
    }

    private List<AddressItem> formFastJson(String content) {
        TypeReference<List<AddressItem>> type = new TypeReference<List<AddressItem>>() {
        };
        return JSON.parseObject(content, type);
    }


}
