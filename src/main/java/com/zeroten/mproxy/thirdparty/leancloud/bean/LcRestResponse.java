package com.zeroten.mproxy.thirdparty.leancloud.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class LcRestResponse<T> {
    private Integer code;
    private String error;

    private String objectId;
    private String updatedAt;

    private List<T> results;

    public boolean isSuccess() {
        return code == null;
    }

    public LcRestResponse<T> putResults(JSONArray jsonArray, Class<T> tClass) {
        if (jsonArray != null) {
            this.results = jsonArray.toJavaList(tClass);
        }
        return this;
    }

    public static <T> LcRestResponse<T> parse(String jsonString, Class<T> tClass) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        LcRestResponse lcRestResponse = jsonObject.toJavaObject(LcRestResponse.class);
        if (lcRestResponse.isSuccess() && tClass != null) {
            lcRestResponse.putResults(jsonObject.getJSONArray("results"), tClass);
        }
        return lcRestResponse;
    }

    public static <T> LcRestResponse<T> parse(String jsonString) {
        return parse(jsonString, null);
    }

}
