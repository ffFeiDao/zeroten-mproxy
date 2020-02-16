package com.zeroten.mproxy.model.request;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class PushBindRequest {
    private String clientId;

    private String clientType;

    private String registrationId;

    private String key;

    private String timestamp;

    private String sign;

    public PushBindRequest(String clientId, String clientType, String registrationId, String key, String timestamp, String sign) {
        this.clientId = clientId;
        this.clientType = clientType;
        this.registrationId = registrationId;
        this.key = key;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("clientId", clientId);
        map.put("clientType", clientType);
        map.put("registrationId", registrationId);
        map.put("key", key);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        return map;
    }
}
