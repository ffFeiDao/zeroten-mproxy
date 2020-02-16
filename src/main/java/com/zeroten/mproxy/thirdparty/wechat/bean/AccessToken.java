package com.zeroten.mproxy.thirdparty.wechat.bean;

import lombok.Data;

@Data
public class AccessToken extends WechatBaseResponse {
    private String accessToken;
    private Integer expiresIn;
    private Long expireTime;

    public boolean isValid() {
        if (!isSuccess()) {
            return false;
        }
        if (expireTime == null) {
            return false;
        }
        return System.currentTimeMillis() < expireTime;
    }

    public String getUrl(String url) {
        return String.format("%s?access_token=%s", url, accessToken);
    }
}
