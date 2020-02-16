package com.zeroten.mproxy.service;

import com.zeroten.mproxy.model.response.BaseResponse;

import java.util.Map;

public interface WechatMessageService {

    String dealReceiveMessage(String contentType, Map<String, String> params, String data);

    String payResultNotify(String contentType, Map<String, String> params, String data);

    BaseResponse<String> getAccessToken(String appId, String key, String timestamp, String sign);

    void test();
}
