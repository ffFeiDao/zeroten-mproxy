package com.zeroten.mproxy.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.dao.OrderFormDao;
import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.dao.WeixinConfigDao;
import com.zeroten.mproxy.dao.WeixinCustomMessageDao;
import com.zeroten.mproxy.entity.OrderForm;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.entity.WeixinCustomMessage;
import com.zeroten.mproxy.enums.SysConfigCodeEnum;
import com.zeroten.mproxy.enums.WxCustomMessageStatusEnum;
import com.zeroten.mproxy.model.bo.OrderOperatingRecord;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.MessageProxyService;
import com.zeroten.mproxy.service.WechatMessageService;
import com.zeroten.mproxy.task.MessageProxyTask;
import com.zeroten.mproxy.thirdparty.wechat.WechatHelper;
import com.zeroten.mproxy.thirdparty.wechat.WechatPayConstants;
import com.zeroten.mproxy.thirdparty.wechat.bean.AccessToken;
import com.zeroten.mproxy.thirdparty.wechat.util.WechatPayUtils;
import com.zeroten.mproxy.util.CheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WechatMessageServiceImpl implements WechatMessageService {
    private static final Logger log = LogManager.getLogger(WechatMessageServiceImpl.class);

    @Autowired
    WeixinCustomMessageDao weixinCustomMessageDao;

    @Autowired
    WeixinConfigDao weixinConfigDao;

    @Autowired
    MessageProxyTask messageProxyTask;

    @Autowired
    MessageProxyService messageProxyService;

    @Autowired
    OrderFormDao orderFormDao;

    @Autowired
    SysConfigDao sysConfigDao;

    Map<String, String> lastSessionFrom = Maps.newConcurrentMap();

    @Override
    public String dealReceiveMessage(String contentType, Map<String, String> params, String data) {
        log.info("receive wechat message: {}", data);

        WeixinCustomMessage message = JSON.parseObject(data, WeixinCustomMessage.class);

        if (!message.isValid()) {
            return Constants.FAIL;
        }

        if (lastSessionFrom.size() > 5000) {
            lastSessionFrom.clear();
        }

        String key = String.format("%s_%s", message.getFromUserName(), message.getToUserName());

        if (message.isUserEnterEvent()) {
            message.setStatus(WxCustomMessageStatusEnum.SUCCESS.name());
            String sessionFrom = message.getSessionFrom();
            if (StringUtils.isNotEmpty(sessionFrom)) {
                lastSessionFrom.put(key, sessionFrom);
            }
        } else {
            String sessionFrom = lastSessionFrom.get(key);
            if (StringUtils.isNotEmpty(sessionFrom)) {
                message.setSessionFrom(sessionFrom);
            }
            message.setStatus(WxCustomMessageStatusEnum.WAIT_FOR_DEAL.name());
        }

        if (sysConfigDao.isTrue(SysConfigCodeEnum.STORE_WECHAT_MESSAGE)) {
            String objectId = weixinCustomMessageDao.save(message);
            message.setObjectId(objectId);
        }

        messageProxyService.deal(message);

        return Constants.SUCCESS;
    }

    @Override
    public String payResultNotify(String contentType, Map<String, String> params, String data) {
        log.info("wechat pay notify：{}, {}, {}", contentType, JSON.toJSONString(params), data);

        try {
            Map<String, String> notifyData = WechatPayUtils.xmlToMap(data);

            String mchId = notifyData.get("mch_id");
            String outTradeNo = notifyData.get("out_trade_no");
            String totalFee = notifyData.get("total_fee");

            if (StringUtils.isNotEmpty(mchId)) {
                WeixinConfig weixinConfig = weixinConfigDao.getByMchId(mchId);
                if (weixinConfig != null && StringUtils.isNotEmpty(weixinConfig.getPayKey())) {
                    if (WechatPayUtils.isPayResultNotifySignatureValid(notifyData, weixinConfig.getPayKey())) {
                        if (WechatPayConstants.SUCCESS.equals(notifyData.get("result_code"))) {
                            OrderForm orderForm = orderFormDao.getOne(weixinConfig.getLcConfig(), OrderForm.class, "number", outTradeNo);
                            if (orderForm == null) {
                                log.warn("not find order：{}", outTradeNo);
                            } else if (!equal(orderForm.getOrderState(), 6)) {
                                Map<String, Object> updateData = Maps.newHashMap();
                                updateData.put("orderState", 6);
                                updateData.put("payMoney", Integer.parseInt(StringUtils.defaultIfEmpty(totalFee, "0")));

                                List<OrderOperatingRecord> opList = OrderOperatingRecord.parseList(orderForm.getOrderOperatingRecord());
                                opList.add(new OrderOperatingRecord("Server", 6));
                                updateData.put("orderOperatingRecord", opList);

                                log.info("update order status:{},{}", orderForm.getObjectId(), outTradeNo);

                                orderFormDao.updateOne(weixinConfig.getLcConfig(), OrderForm.class, orderForm.getObjectId(), updateData);
                            } else {
                                log.info("order paid：{}", outTradeNo);
                            }
                        } else {
                            log.info("wechat pay fail：{}", data);
                        }
                    } else {
                        log.info("wechat pay notify sign check fail：{}", data);
                    }
                } else {
                    log.error("wechat pay config not exist：{}", data);
                }
            } else {
                log.error("wechat pay config mch_id is empty：{}", data);
            }
        } catch (Exception e) {
            log.error("wechat pay notify deal error：{}", data, e);
        }

        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    @Override
    public BaseResponse<String> getAccessToken(String appId, String key, String timestamp, String sign) {
        if (StringUtils.isEmpty(appId)) {
            return new BaseResponse(4, "参数appId不正确");
        }

        Map<String, String> params = Maps.newHashMap();
        params.put("appId", appId);
        params.put("timestamp", timestamp);
        params.put("key", key);

        if (!CheckUtils.isNotEmpty(timestamp, key)) {
            return new BaseResponse(4, "参数不正确");
        }

        String signCalc = CheckUtils.calcSign(sysConfigDao, params);

        log.info("request {},{},{}", sign, signCalc, JSON.toJSONString(params));

        if (!StringUtils.equals(sign, signCalc)) {
            return new BaseResponse(4, "签名错误");
        }

        WeixinConfig weixinConfig = weixinConfigDao.getByAppId(appId);
        if (weixinConfig == null) {
            return new BaseResponse(4, "应用id不存在");
        }

        AccessToken accessToken = WechatHelper.getAccessToken(weixinConfig);
        if (accessToken == null) {
            return new BaseResponse(4, "token获取失败");
        } else if (!accessToken.isValid()) {
            return new BaseResponse(4, accessToken.getAccessToken());
        }

        return new BaseResponse<>(0, accessToken.getAccessToken());
    }

    @Override
    public void test() {
    }

    private boolean equal(Integer n1, int n2) {
        return n1 != null && n1 == n2;
    }

}
