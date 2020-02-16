package com.zeroten.mproxy.thirdparty.jiguang;

import lombok.Data;

@Data
public class PushConfig {
    private String key;
    private String secret;
    private Boolean apnsProduction;

    public boolean hasSetAPNS() {
        return apnsProduction != null;
    }
}
