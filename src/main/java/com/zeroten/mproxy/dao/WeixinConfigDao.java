package com.zeroten.mproxy.dao;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.WeixinConfig;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeixinConfigDao extends BaseDao<WeixinConfig> {

    public WeixinConfig getByAppId(String appId) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("appId", appId);

        return findOne(WeixinConfig.class, where);
    }

    public WeixinConfig getByGhId(String ghId) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("ghId", ghId);

        return findOne(WeixinConfig.class, where);
    }

    public WeixinConfig getByMchId(String mchId) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("mchId", mchId);

        return findOne(WeixinConfig.class, where);
    }

    public WeixinConfig getByLcAppId(String lcAppId) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("lcAppId", lcAppId);

        return findOne(WeixinConfig.class, where);
    }

}
