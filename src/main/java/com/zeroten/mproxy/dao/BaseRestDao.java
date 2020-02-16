package com.zeroten.mproxy.dao;

import cn.leancloud.core.RequestPaddingInterceptor;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.BaseEntity;
import com.zeroten.mproxy.thirdparty.leancloud.LcConfig;
import com.zeroten.mproxy.thirdparty.leancloud.bean.LcRestResponse;
import com.zeroten.mproxy.util.BaseClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 直接通过 RESTful 接口操作数据
 */
public class BaseRestDao<T extends BaseEntity> {
    private static final Logger log = LogManager.getLogger(BaseRestDao.class);

    private List<T> list(LcConfig lcConfig, Class<T> tClass, Map<String, Object> where) {
        String url = String.format("%s/1.1/classes/%s", lcConfig.getRestUrl(), getStorageName(tClass));

        try {
            Map<String, String> params = Maps.newHashMap();
            params.put("where", JSON.toJSONString(where));

            String respString = new BaseClient().get(url, getLcAuthHeader(lcConfig), params);
            LcRestResponse<T> lcRestResponse = LcRestResponse.parse(respString, tClass);
            return lcRestResponse.getResults() == null ? Lists.newArrayList() : lcRestResponse.getResults();
        } catch (UnsupportedEncodingException e) {
            log.error("LeanCloud REST Error", e);
        } catch (IOException e) {
            log.error("LeanCloud REST Error", e);
        }

        return Lists.newArrayList();
    }

    public boolean updateOne(LcConfig lcConfig, Class<T> tClass, String objectId, Map<String, Object> data) {
        String url = String.format("%s/1.1/classes/%s/%s", lcConfig.getRestUrl(), getStorageName(tClass), objectId);

        try {
            String respString = new BaseClient().put(url, getLcAuthHeader(lcConfig), JSON.toJSONString(data));
            LcRestResponse lcRestResponse = LcRestResponse.parse(respString);
            return lcRestResponse.isSuccess();
        } catch (UnsupportedEncodingException e) {
            log.error("LeanCloud REST Error", e);
        } catch (IOException e) {
            log.error("LeanCloud REST Error", e);
        }
        return false;
    }

    public T getOne(LcConfig lcConfig, Class<T> tClass, Map<String, Object> where) {
        List<T> list = list(lcConfig, tClass, where);
        return list.size() == 1 ? list.get(0) : null;
    }

    public T getOne(LcConfig lcConfig, Class<T> tClass, String field, Object value) {
        Map<String, Object> where = Maps.newHashMap();
        where.put(field, value);

        return getOne(lcConfig, tClass, where);
    }

    protected String getStorageName(Class tClass) {
        return tClass.getSimpleName();
    }

    private Map<String, String> getLcAuthHeader(LcConfig lcConfig) {
        Map<String, String> header = Maps.newHashMap();
        header.put(RequestPaddingInterceptor.HEADER_KEY_LC_APPID, lcConfig.getAppId());
        header.put(RequestPaddingInterceptor.HEADER_KEY_LC_APPKEY, String.format("%s,master", lcConfig.getMasterKey()));
        header.put("Content-Type", "application/json");
        return header;
    }
}
