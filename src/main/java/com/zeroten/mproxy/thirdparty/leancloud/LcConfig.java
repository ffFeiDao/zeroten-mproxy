package com.zeroten.mproxy.thirdparty.leancloud;

import lombok.Data;

@Data
public class LcConfig {
    private String appId;
    private String appKey;
    private String masterKey;
    private String restUrl;
}
