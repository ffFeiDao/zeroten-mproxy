package com.zeroten.mproxy.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.dao.AccountDao;
import com.zeroten.mproxy.dao.PushBindDao;
import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.dao.WeixinConfigDao;
import com.zeroten.mproxy.entity.Account;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.model.request.AccountRequest;
import com.zeroten.mproxy.model.request.OrderQueryRequest;
import com.zeroten.mproxy.model.request.PushBindRequest;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.PushService;
import com.zeroten.mproxy.thirdparty.wechat.pay.WechatPayTool;
import com.zeroten.mproxy.thirdparty.wechat.pay.bean.WechatPayOrderQueryResponse;
import com.zeroten.mproxy.util.CheckUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PushServiceImpl implements PushService {
    private static final Logger log = LogManager.getLogger(PushServiceImpl.class);

    @Autowired
    AccountDao accountDao;

    @Autowired
    PushBindDao pushBindDao;

    @Autowired
    SysConfigDao sysConfigDao;

    @Autowired
    WeixinConfigDao weixinConfigDao;

    Map<String, Set<Long>> reqTimestampCache = Maps.newConcurrentMap();

    @Override
    public BaseResponse<List<Account>> getAccount(String mobile, String key, String timestamp, String sign) {
        Map<String, String> params = Maps.newHashMap();
        params.put("telephone", mobile);
        params.put("timestamp", timestamp);
        params.put("key", key);

        if (!CheckUtils.isNotEmpty(mobile, timestamp, key)) {
            return new BaseResponse(4, "参数不正确");
        }

        String signCalc = CheckUtils.calcSign(sysConfigDao, params);

        log.info("request {},{},{}", sign, signCalc, JSON.toJSONString(params));

        if (!StringUtils.equals(sign, signCalc)) {
            return new BaseResponse(4, "签名错误");
        }

        ReqError reqError = checkRequest(mobile, timestamp);
        if (!reqError.isSuccess()) {
            return new BaseResponse(4, reqError.getText());
        }

        if (StringUtils.isEmpty(mobile)) {
            return new BaseResponse(4, "手机号码不能为空");
        }
        return new BaseResponse<>(accountDao.listByTelephone(mobile));
    }

    @Override
    public BaseResponse updateAccount(AccountRequest request) {
        if (!CheckUtils.isNotEmpty(request.getClientId(), request.getTimestamp(), request.getKey(), request.getSign())) {
            return new BaseResponse(4, "参数不正确");
        }
        if (request.getClerkState() == null) {
            return new BaseResponse(4, "参数不正确");
        }

        Map<String, String> params = request.toMap();

        String signCalc = CheckUtils.calcSign(sysConfigDao, params);

        log.info("request {},{},{}", request.getSign(), signCalc, JSON.toJSONString(params));

        if (!StringUtils.equals(request.getSign(), signCalc)) {
            return new BaseResponse(4, "签名错误");
        }

        ReqError reqError = checkRequest(request.getClientId(), request.getTimestamp());
        if (!reqError.isSuccess()) {
            return new BaseResponse(4, reqError.getText());
        }

        return new BaseResponse<>(accountDao.update(request.getClientId(), request.getClerkState()));
    }

    private ReqError checkRequest(String mobile, String timestamp) {
        Set<Long> timestampList;
        if (reqTimestampCache.containsKey(mobile)) {
            timestampList = reqTimestampCache.get(mobile);
        } else {
            timestampList = Sets.newHashSet();
            reqTimestampCache.put(mobile, timestampList);
        }

        long timeout = 600000;

        try {
            long now = System.currentTimeMillis();
//            DateTime time = DateTime.parse(timestamp, DateTimeFormat.forPattern("yyyyMMddHHmmss"));

            removeTimeoutTimestamp(timestampList, now, timeout);

//            if (Math.abs(now - time.getMillis()) > timeout) {
//                return ReqError.TIMEOUT;
//            }
            long timestampN = Long.parseLong(timestamp);
            if (timestampList.contains(timestampN)) {
                return ReqError.REPEAT;
            }

            timestampList.add(timestampN);

            return ReqError.SUCCESS;
        } catch (Exception e) {
        }

        return ReqError.ERROR;
    }

    private void removeTimeoutTimestamp(Set<Long> set, long now, long timeout) {
        Iterator<Long> iterator = set.iterator();
        while (iterator.hasNext()) {
            if (now - iterator.next() >= timeout) {
                iterator.remove();
            }
        }
    }

    @Getter
    enum ReqError {
        SUCCESS("OK"),
        ERROR("ERROR"),
        TIMEOUT("APP和服务器时间差超过10分钟"),
        REPEAT("重复请求");

        private String text;

        ReqError(String text) {
            this.text = text;
        }

        public boolean isSuccess() {
            return SUCCESS.equals(this);
        }
    }

    @Override
    public BaseResponse bind(PushBindRequest request) {
        if (StringUtils.isEmpty(request.getClientType())) {
            return new BaseResponse(4, "客户端类型不能为空");
        }
        if (StringUtils.isEmpty(request.getClientId())) {
            return new BaseResponse(4, "客户端id不能为空");
        }
        if (StringUtils.isEmpty(request.getRegistrationId())) {
            return new BaseResponse(4, "极光推送id不能为空");
        }

        if (!CheckUtils.isNotEmpty(request.getKey(), request.getTimestamp(), request.getSign())) {
            return new BaseResponse(4, "参数不正确");
        }

        Map<String, String> reqMap = request.toMap();

        log.info("jiguang push bind: {}", JSON.toJSONString(reqMap));

        String signCalc = CheckUtils.calcSign(sysConfigDao, reqMap);

        if (!StringUtils.equals(request.getSign(), signCalc)) {
            return new BaseResponse(4, "签名错误");
        }

        ReqError reqError = checkRequest(request.getClientId(), request.getTimestamp());
        if (!reqError.isSuccess()) {
            return new BaseResponse(4, reqError.getText());
        }

        Account account = accountDao.getByClientId(request.getClientId());
        if (account == null) {
            return new BaseResponse(4, "客户端id不存在");
        }
        accountDao.update(request.getClientId(), request.getClientType(), request.getRegistrationId());

        return new BaseResponse(0, Constants.SUCCESS);
    }

    @Override
    public BaseResponse unbind(PushBindRequest request) {
        if (StringUtils.isEmpty(request.getClientId())) {
            return new BaseResponse(4, "client_id不能为空");
        }

        Account account = accountDao.getByClientId(request.getClientId());
        if (account == null) {
            return new BaseResponse(4, "客户端id不存在");
        }
        accountDao.update(request.getClientId(), "", "");

        return new BaseResponse(0, Constants.SUCCESS);
    }

    @Override
    public BaseResponse orderQuery(OrderQueryRequest request) {
        if (StringUtils.isEmpty(request.getOrderNo())) {
            return new BaseResponse(4, "订单编号不能为空");
        }

        String signCalc = CheckUtils.calcSign(sysConfigDao, request.toMap());

        if (!StringUtils.equals(request.getSign(), signCalc)) {
            return new BaseResponse(4, "签名错误");
        }

        ReqError reqError = checkRequest(request.getOrderNo(), request.getTimestamp());
        if (!reqError.isSuccess()) {
            return new BaseResponse(4, reqError.getText());
        }

        WeixinConfig config = null;
        if (StringUtils.isNotEmpty(request.getAppId())) {
            config = weixinConfigDao.getByAppId(request.getAppId());
        }
        if (config == null && StringUtils.isNotEmpty(request.getClAppId())) {
            config = weixinConfigDao.getByLcAppId(request.getClAppId());
        }
        if (config == null) {
            return new BaseResponse(4, "未找到支付配置");
        }

        WechatPayOrderQueryResponse wxResponse = new WechatPayTool(config).orderQuery(null, request.getOrderNo());

        BaseResponse response = new BaseResponse(0, Constants.SUCCESS);

        response.setData(wxResponse);

        return response;
    }

}
