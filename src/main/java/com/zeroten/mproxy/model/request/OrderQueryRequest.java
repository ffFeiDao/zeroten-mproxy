package com.zeroten.mproxy.model.request;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class OrderQueryRequest {
    private String appId;

    private String clAppId;

    private String orderNo;

    private String key;

    private String timestamp;

    private String sign;

    public OrderQueryRequest(String appId, String clAppId, String orderNo, String key, String timestamp, String sign) {
        this.appId = appId;
        this.clAppId = clAppId;
        this.orderNo = orderNo;
        this.key = key;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("appId", appId);
        map.put("clAppId", clAppId);
        map.put("orderNo", orderNo);
        map.put("key", key);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        return map;
    }
}
