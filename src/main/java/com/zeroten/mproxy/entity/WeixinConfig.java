package com.zeroten.mproxy.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.zeroten.mproxy.thirdparty.leancloud.LcConfig;
import lombok.Data;

/**
 * 微信配置
 */
@Data
public class WeixinConfig extends BaseEntity {
    /**
     * 小程序原始id
     */
    private String ghId;

    /**
     * 小程序AppId
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;

    /**
     * 消息 Token 令牌
     */
    private String token;

    /**
     * 消息加密 AesKey
     */
    private String aesKey;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付 API 密钥
     */
    private String payKey;

    /**
     * 小程序对应 LeanCloud 应用配置
     */
    private String lcAppId;
    private String lcAppKey;
    private String lcMasterKey;
    private String lcRestUrl;

    @JSONField(serialize = false)
    public LcConfig getLcConfig() {
        LcConfig lcConfig = new LcConfig();

        lcConfig.setAppId(lcAppId);
        lcConfig.setAppKey(lcAppKey);
        lcConfig.setMasterKey(lcMasterKey);
        lcConfig.setRestUrl(lcRestUrl);

        return lcConfig;
    }
}
