package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.zeroten.mproxy.thirdparty.jiguang.enums.Platform;
import lombok.Data;

import java.util.List;

@Data
public class PushRequest {
    private List<Platform> platform;

    private Audience audience;

    private Notification notification;

    private Message message;

    @JSONField(name = "sms_message")
    private String smsMessage;

    private PushOptions options;

    private String cid;

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
