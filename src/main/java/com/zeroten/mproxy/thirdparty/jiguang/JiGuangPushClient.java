package com.zeroten.mproxy.thirdparty.jiguang;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.thirdparty.jiguang.enums.Platform;
import com.zeroten.mproxy.thirdparty.jiguang.model.*;
import com.zeroten.mproxy.util.BaseClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

public class JiGuangPushClient {
    private static final String URL_PUSH = "https://api.jpush.cn/v3/push";

    private PushConfig config;
    private Map<String, String> authHeader = Maps.newHashMap();

    public JiGuangPushClient setConfig(PushConfig config) {
        this.config = config;
        authHeader.put("Authorization", "Basic " + Base64.encodeBase64String((config.getKey() + ":" + config.getSecret()).getBytes()));
        return this;
    }

    public PushResponse push(PushContent content) {
        Audience audience = new Audience();
        audience.setRegistrationId(Lists.newArrayList(content.getClientId()));

        Message message = new Message();
        message.setTitle(content.getTitle());
        message.setMsgContent(content.getContent());

        NotificationAndroid notifyAndroid = null;
        NotificationIos notifyIos = null;

        PushRequest pushRequest = new PushRequest();
        if (content.getClientType().isAll()) {
            pushRequest.setPlatform(Lists.newArrayList(Platform.android, Platform.ios));

            notifyAndroid = new NotificationAndroid();
            notifyAndroid.setTitle(message.getTitle());
            notifyAndroid.setAlert(message.getMsgContent());

            notifyIos = new NotificationIos();
            notifyIos.setAlert(StringUtils.defaultIfEmpty(message.getMsgContent(), message.getTitle()));

            if (config.hasSetAPNS()) {
                PushOptions pushOptions = new PushOptions();
                pushOptions.setApnsProduction(config.getApnsProduction());
                pushRequest.setOptions(pushOptions);
            }
        } else {
            pushRequest.setPlatform(Lists.newArrayList(Platform.of(content.getClientType())));

            if (content.getClientType().isAndroid()) {
                notifyAndroid = new NotificationAndroid();
                notifyAndroid.setTitle(message.getTitle());
                notifyAndroid.setAlert(message.getMsgContent());
            } else {
                notifyIos = new NotificationIos();
                notifyIos.setAlert(StringUtils.defaultIfEmpty(message.getMsgContent(), message.getTitle()));

                if (config.hasSetAPNS()) {
                    PushOptions pushOptions = new PushOptions();
                    pushOptions.setApnsProduction(config.getApnsProduction());
                    pushRequest.setOptions(pushOptions);
                }
            }
        }
        pushRequest.setAudience(audience);
        pushRequest.setMessage(message);

        Notification notification = new Notification(notifyAndroid, notifyIos);
        notification.setAlert(StringUtils.defaultIfEmpty(message.getMsgContent(), message.getTitle()));

        pushRequest.setNotification(notification);

        String pushJSONString = pushRequest.toJSONString();

        try {
            String respString = new BaseClient().post(URL_PUSH, getAuthHeader(), pushJSONString);
            return JSON.parseObject(respString, PushResponse.class);
        } catch (IOException e) {
            return new PushResponse(null, e.getMessage());
        }
    }

    private Map<String, String> getAuthHeader() {
        return authHeader;
    }
}
