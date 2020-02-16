package com.zeroten.mproxy.model.request;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class AccountRequest {
    private String clientId;
    private Integer clerkState;
    private String key;
    private String timestamp;
    private String sign;

    public AccountRequest(String clientId, Integer clerkState, String key, String timestamp, String sign) {
        this.clientId = clientId;
        this.clerkState = clerkState;
        this.key = key;
        this.timestamp = timestamp;
        this.sign = sign;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("clientId", clientId);
        map.put("clerkState", clerkState == null ? "" : clerkState.toString());
        map.put("key", key);
        map.put("timestamp", timestamp);
        map.put("sign", sign);
        return map;
    }
}
