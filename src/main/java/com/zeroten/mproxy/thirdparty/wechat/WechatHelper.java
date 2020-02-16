package com.zeroten.mproxy.thirdparty.wechat;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.thirdparty.wechat.bean.AccessToken;
import com.zeroten.mproxy.thirdparty.wechat.bean.WechatBaseResponse;
import com.zeroten.mproxy.thirdparty.wechat.bean.WechatUploadTempMediaResponse;
import com.zeroten.mproxy.thirdparty.wechat.msg.WechatMessage;
import com.zeroten.mproxy.util.BaseClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class WechatHelper {
    private static final Logger log = LogManager.getLogger(WechatHelper.class);

    private static final Map<String, AccessToken> accessTokenCache = Maps.newConcurrentMap();
    WeixinConfig config;

    public WechatHelper(WeixinConfig config) {
        this.config = config;
    }

    public WechatBaseResponse sendMessage(String openid, WechatMessage wechatMessage) {
        AccessToken accessToken = getAccessToken(config);
        if (accessToken == null || !accessToken.isValid()) {
            return new WechatBaseResponse("4", accessToken == null ? "get token fail" : accessToken.getErrmsg());
        }

        try {
            String messageJSONString = wechatMessage.toJSONString(openid, this);
            String url = accessToken.getUrl("https://api.weixin.qq.com/cgi-bin/message/custom/send");
            String respString = new BaseClient().post(url, null, messageJSONString);
            log.info("send message: {}, {}, {}", config.getAppId(), messageJSONString, respString);
            return JSON.parseObject(respString, WechatBaseResponse.class);
        } catch (IOException e) {
            log.error("send message error: {}, {}", config.getAppId(), openid, e);
            return new WechatBaseResponse("4", "send message error: " + e.getMessage());
        }
    }

    public WechatUploadTempMediaResponse uploadTempMedia(String imageUrl) {
        AccessToken accessToken = getAccessToken(config);
        if (accessToken == null || !accessToken.isValid()) {
            return null;
        }

        String url = String.format("%s&type=image", accessToken.getUrl("https://api.weixin.qq.com/cgi-bin/media/upload"));
        try {
            byte[] imageData = new BaseClient().getData(imageUrl);
            Map<String, Object> files = Maps.newHashMap();
            files.put("media", imageData);
            String respString = new BaseClient().upload(url, null, null, files);
            return JSON.parseObject(respString, WechatUploadTempMediaResponse.class);
        } catch (IOException e) {
            log.error("update wechat temp media error", e);
            return new WechatUploadTempMediaResponse("4", "upload error " + e.getMessage());
        }
    }

    public String getMediaDownUrl(String mediaId) {
        AccessToken accessToken = getAccessToken(config);
        if (accessToken == null || !accessToken.isValid()) {
            return null;
        }

        return String.format("%s&media_id=%s", accessToken.getUrl("https://api.weixin.qq.com/cgi-bin/media/get"), mediaId);
    }

    public static AccessToken getAccessToken(WeixinConfig config) {
        AccessToken accessToken = accessTokenCache.get(config.getAppId());
        if (accessToken == null || !accessToken.isValid()) {
            accessToken = reGet(config);
        }
        return accessToken;
    }

    private static AccessToken reGet(WeixinConfig config) {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                config.getAppId(), config.getAppSecret());
        try {
            String respString = new BaseClient().get(url);
            AccessToken accessToken = JSON.parseObject(respString, AccessToken.class);
            if (StringUtils.isNotEmpty(accessToken.getAccessToken())) {
                accessToken.setExpireTime(System.currentTimeMillis() + accessToken.getExpiresIn());
            }
            log.info("get wechat access token: {}, {}", config.getAppId(), accessToken.isValid() ? accessToken.getAccessToken() : accessToken.getErrmsg());
            return accessToken;
        } catch (IOException e) {
            log.info("get wechat access token fail: {}", config.getAppId(), e);
        }

        return null;
    }

}
