package com.zeroten.mproxy.thirdparty.leancloud.msg;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class LcProductMessage extends LcMessage {
    private String objectId;
    private String title;
    private String url;
    private String pagePath;

    @Override
    public String getPushTitle() {
        return "一条单品消息";
    }

    @Override
    public String getPushContent() {
        return title;
    }

    @Override
    public Map toLcTextObject() {
        Map msg = Maps.newHashMap();
        msg.put("_lctype", -1);
        msg.put("_lcattrs", new Object());

        Map<String, Object> _lcproduct = Maps.newHashMap();
        _lcproduct.put("_objectId", objectId);
        _lcproduct.put("_title", title);
        _lcproduct.put("_url", url);
        _lcproduct.put("_pagePath", pagePath);

        msg.put("_lctext", JSON.toJSONString(_lcproduct));

        return msg;
    }
}
