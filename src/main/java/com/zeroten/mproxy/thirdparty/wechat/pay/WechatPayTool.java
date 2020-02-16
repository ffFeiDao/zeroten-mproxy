package com.zeroten.mproxy.thirdparty.wechat.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.thirdparty.wechat.WechatPayConstants;
import com.zeroten.mproxy.thirdparty.wechat.WechatPayConstants.SignType;
import com.zeroten.mproxy.thirdparty.wechat.pay.bean.WechatPayOrderQueryResponse;
import com.zeroten.mproxy.thirdparty.wechat.util.WechatPayUtils;
import com.zeroten.mproxy.util.BaseClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WechatPayTool {
    private static final Logger log = LogManager.getLogger(WechatPayTool.class);

    WeixinConfig config;

    public WechatPayTool(WeixinConfig config) {
        this.config = config;
    }

    public WechatPayOrderQueryResponse orderQuery(String transactionId, String outTradeNo) {
        Map<String, String> data = Maps.newHashMap();
        if (StringUtils.isNotEmpty(transactionId)) {
            data.put("transaction_id", transactionId);
        }
        if (StringUtils.isNotEmpty(outTradeNo)) {
            data.put("out_trade_no", outTradeNo);
        }

        String url = "https://api.mch.weixin.qq.com/pay/orderquery";

        try {
            SignType signType = SignType.HMACSHA256;
            Map<String, String> reqData = WechatPayUtils.fillRequestData(data, signType, config);
            String xmlString = WechatPayUtils.mapToXml(reqData);

            String respString = new BaseClient().post(url, null, xmlString);

            Map<String, String> rspData = WechatPayUtils.processResponseXml(respString, signType, config);

            return new JSONObject(map(rspData)).toJavaObject(WechatPayOrderQueryResponse.class);
        } catch (Exception e) {
            log.error("微信订单查询错误:{}", JSON.toJSONString(data), e);

            return new WechatPayOrderQueryResponse(WechatPayConstants.FAIL, "查询错误:" + e.getMessage());
        }
    }


    private Map<String, Object> map(Map<String, String> map) {
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(map.size());
        map.forEach((key, value) -> data.put(key, value));
        return data;
    }
}
