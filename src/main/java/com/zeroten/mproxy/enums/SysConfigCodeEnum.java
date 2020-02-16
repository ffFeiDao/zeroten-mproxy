package com.zeroten.mproxy.enums;

import lombok.Getter;

@Getter
public enum SysConfigCodeEnum {

    /**
     * 七牛配置
     */
    QINIU_ACCESSKEY("七牛accessKey"),
    QINIU_SECRETKEY("七牛secretKey"),
    QINIU_BUCKET("七牛bucket"),
    QINIU_URL("七牛访问Url"),

    /**
     * LeanCloud 消息服务配置
     */
    LC_IM_APPID("LeanCloud即时通讯 AppId"),
    LC_IM_APPKEY("LeanCloud即时通讯 AppKey"),
    LC_IM_MASTERKEY("LeanCloud即时通讯 MasterKey"),
    LC_IM_REST_URL("LeanCloud即时通讯 REST API 服务器地址"),

    /**
     * 极光
     */
    JG_KEY("极光Key"),
    JG_SECRET("极光Secret"),
    JG_APNS("iOS推送环境"),

    /**
     * 系统配置
     */
    STORE_WECHAT_MESSAGE("是否保持微信消息"),
    RESET_CACHE_PASSWORD("重置缓存密码"),
    API_KEY("Api接口Key"),
    API_SECRET("Api接口密钥"),

    ;

    private String text;

    SysConfigCodeEnum(String text) {
        this.text = text;
    }
}
