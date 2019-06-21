package me.ztiany.jsonanalyze.model;

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
public class ModelParser {

    private static final String TAG = ModelParser.class.getSimpleName();

    private static final String PATH_1000 = "1000_json.json";
    private static final String PATH_3 = "3_json.json";

    private final String mContent;

    public static final int PARSER_TYPE_GSON = 1;
    public static final int PARSER_TYPE_FASTJSON = 2;

    public ModelParser(boolean bigData) {
        mContent = getAddressJson(bigData);
    }

    public long start(int parserType) {
        final long start = System.currentTimeMillis();
        List<SampleEntity> items = parse(parserType, mContent);
        Log.d(TAG, "SampleEntity count = " + items.size());
        long time = System.currentTimeMillis() - start;
        Log.d(TAG, getParserName(parserType) + " use time = " + time);
        return time;
    }

    private String getParserName(int parserType) {
        return parserType == PARSER_TYPE_GSON ? "GSON" : "FASTJSON";
    }

    private List<SampleEntity> parse(int parserType, String s) {
        if (parserType == PARSER_TYPE_GSON) {
            return formGson(s);
        } else {
            return formFastJson(s);
        }
    }

    private String getAddressJson(boolean bigData) {
        try {
            String path = bigData ? PATH_1000 : PATH_3;
            return IOUtils.convertToString(AppContext.getAppContext().getAssets().open(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private List<SampleEntity> formGson(String content) {
        final Type type = new TypeToken<List<SampleEntity>>() {
        }.getType();

        return new Gson().fromJson(content, type);
    }

    private List<SampleEntity> formFastJson(String content) {
        TypeReference<List<SampleEntity>> type = new TypeReference<List<SampleEntity>>() {
        };
        return JSON.parseObject(content, type);
    }


}
