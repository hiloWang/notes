package com.ztiany.mall.web.bean;

import com.google.gson.reflect.TypeToken;
import com.ztiany.mall.utils.JsonUtils;
import com.ztiany.mall.utils.StringChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录管理工具，默认的最大记录数为：10
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.19 11:28
 */
public class History {

    private final int maxRecord;
    private List<String> historyList;

    public History(String historyJson) {
        this(10, historyJson);
    }

    public History(int maxRecord, String historyJson) {
        this.maxRecord = maxRecord;
        historyList = parse(historyJson);
        if (historyList.size() > maxRecord) {
            historyList = historyList.subList(0, maxRecord);
        }
    }

    public void add(String history) {
        if (historyList.contains(history)) {
            historyList.remove(history);
            historyList.add(0, history);
        } else {
            historyList.add(0, history);
            if (historyList.size() > maxRecord) {
                historyList.remove(historyList.size() - 1);
            }
        }
    }

    public String getHistoryJson() {
        return JsonUtils.toJson(historyList);
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    private List<String> parse(String historyStr) {
        System.out.println(historyStr);
        if (StringChecker.isEmpty(historyStr)) {
            return new ArrayList<>();
        }
        return JsonUtils.fromType(historyStr, new TypeToken<List<String>>() {
        }.getType());
    }

}
