package com.ztiany.translate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.10.15 15:57
 */
public class Result {

    /**
     * translation : ["的名字"]
     * basic : {"us-phonetic":"nem","phonetic":"neɪm","uk-phonetic":"neɪm","explains":["n. 名称，名字；姓名；名誉","adj. 姓名的；据以取名的","vt. 命名，任命；指定；称呼；提名；叫出","n. (Name)人名；(日)滑(姓)；(英)内姆"]}
     * query : name
     * errorCode : 0
     * web : [{"value":["姓名","名称","名字"],"key":"Name"},{"value":["名字","名","用汉语拼音表示"],"key":"first name"},{"value":["笔名","笔名","囚犯的号码"],"key":"pen name"}]
     */

    private BasicEntity basic;
    private String query;
    private int errorCode;
    private List<String> translation;
    private List<WebEntity> web;

    public BasicEntity getBasic() {
        return basic;
    }

    public void setBasic(BasicEntity basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebEntity> getWeb() {
        return web;
    }

    public void setWeb(List<WebEntity> web) {
        this.web = web;
    }

    public static class BasicEntity {

        /**
         * us-phonetic : nem
         * phonetic : neɪm
         * uk-phonetic : neɪm
         * explains : ["n. 名称，名字；姓名；名誉","adj. 姓名的；据以取名的","vt. 命名，任命；指定；称呼；提名；叫出","n. (Name)人名；(日)滑(姓)；(英)内姆"]
         */

        @SerializedName("us-phonetic")
        private String usphonetic;
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String ukphonetic;
        private List<String> explains;

        public String getUsphonetic() {
            return usphonetic;
        }

        public void setUsphonetic(String usphonetic) {
            this.usphonetic = usphonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUkphonetic() {
            return ukphonetic;
        }

        public void setUkphonetic(String ukphonetic) {
            this.ukphonetic = ukphonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        @Override
        public String toString() {
            return "BasicEntity{" +
                    "usphonetic='" + usphonetic + '\'' +
                    ", phonetic='" + phonetic + '\'' +
                    ", ukphonetic='" + ukphonetic + '\'' +
                    ", explains=" + explains +
                    '}';
        }
    }

    public static class WebEntity {

        /**
         * value : ["姓名","名称","名字"]
         * key : Name
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "WebEntity{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "basic=" + basic +
                ", query='" + query + '\'' +
                ", errorCode=" + errorCode +
                ", translation=" + translation +
                ", web=" + web +
                '}';
    }
}
