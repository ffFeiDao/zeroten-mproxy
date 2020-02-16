package com.zeroten.mproxy.thirdparty.leancloud.msg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.WeixinCustomMessage;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public abstract class LcMessage {
    public static LcMessage parseFrom(String jsonString) {
        if (!jsonString.startsWith("{") || !jsonString.endsWith("}")) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(jsonString);
        Integer _lctype = jsonObject.getInteger("_lctype");
        String _lctext = jsonObject.getString("_lctext");
        JSONObject _lcfile = jsonObject.getJSONObject("_lcfile");
        if (_lctype == null) {
            return null;
        }

        if (_lctype == -1) {
            if (_lctext.startsWith("{") && _lctext.endsWith("}")) {
                LcProductMessage lcProductMessage = new LcProductMessage();
                JSONObject obj = JSON.parseObject(_lctext);
                lcProductMessage.setObjectId(obj.getString("_objectId"));
                lcProductMessage.setTitle(obj.getString("_title"));
                lcProductMessage.setUrl(obj.getString("_url"));
                lcProductMessage.setPagePath(obj.getString("_pagePath"));
                return lcProductMessage;
            }
            return new LcTextMessage(_lctext);
        }

        if (_lctype == -2) {
            LcImageMessage lcImageMessage = new LcImageMessage();
            lcImageMessage.setUrl(_lcfile.getString("url"));
            lcImageMessage.setObjId(_lcfile.getString("objId"));
            JSONObject metaData = _lcfile.getJSONObject("metaData");
            if (metaData != null) {
                lcImageMessage.setName(metaData.getString("name"));
                lcImageMessage.setFormat(metaData.getString("format"));
            }
            return lcImageMessage;
        }

        return null;
    }

    public static LcMessage parseFrom(WeixinCustomMessage weixinCustomMessage) {
        if (weixinCustomMessage == null) {
            return null;
        }
        if (weixinCustomMessage.isUserEnterEvent()) {
            return null;
        }
        if (weixinCustomMessage.isTextMessage()) {
            return new LcTextMessage(weixinCustomMessage.getContent());
        }
        if (weixinCustomMessage.isImageMessage()) {
            LcImageMessage lcImageMessage = new LcImageMessage();
            lcImageMessage.setUrl(weixinCustomMessage.getQiniuUrl());
            lcImageMessage.setObjId("");
            lcImageMessage.setName("1.png");
            lcImageMessage.setFormat("image/png");
            return lcImageMessage;
        }
        if (weixinCustomMessage.isMiniProgramPage()) {
            LcProductMessage lcProductMessage = new LcProductMessage();
            String pagePath = weixinCustomMessage.getPagePath();
            String id = "";
            if (StringUtils.isNotEmpty(pagePath)) {
                int a = pagePath.indexOf("id=");
                int b = a > 0 ? pagePath.indexOf("&", a) : 0;
                if (a > 0 && b > 0) {
                    id = pagePath.substring(a + 3, b);
                } else if (a > 0) {
                    id = pagePath.substring(a + 3);
                }
            }
            lcProductMessage.setObjectId(id);
            lcProductMessage.setTitle(weixinCustomMessage.getTitle());
            lcProductMessage.setUrl(weixinCustomMessage.getQiniuUrl());
            lcProductMessage.setPagePath(weixinCustomMessage.getPagePath());
            return lcProductMessage;
        }
        return null;
    }

    public abstract String getPushTitle();

    public abstract String getPushContent();

    public abstract Map toLcTextObject();

    public String toLcText(String fromClient) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("from_client", fromClient);

        params.put("message", JSON.toJSONString(toLcTextObject()));

        return JSON.toJSONString(params);
    }

}
