package com.zeroten.mproxy.service.impl;

import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.dao.AccountDao;
import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.dao.WeixinConfigDao;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.enums.SysConfigCodeEnum;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.ResetService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ResetServiceImpl implements ResetService {
    private static final Logger log = LogManager.getLogger(ResetServiceImpl.class);

    @Autowired
    SysConfigDao sysConfigDao;

    @Autowired
    WeixinConfigDao weixinConfigDao;

    @Autowired
    AccountDao accountDao;

    @Override
    public BaseResponse reset(String password) {
        if (StringUtils.isEmpty(password) || !StringUtils.equals(password, sysConfigDao.getByCode(SysConfigCodeEnum.RESET_CACHE_PASSWORD))) {
            return new BaseResponse(4, "重置密码错误");
        }

        sysConfigDao.clearCache();
        accountDao.clearCache();

        WeixinConfig weixinConfig = weixinConfigDao.getByAppId("小程序AppId");
        if (weixinConfig == null) {
            weixinConfig = new WeixinConfig();
            weixinConfig.setAppId("小程序AppId");
        }
        weixinConfig.setGhId("小程序原始id");
        weixinConfig.setAppSecret("小程序AppSecret");
        weixinConfig.setToken("小程序消息Token(令牌)");
        weixinConfig.setAesKey("小程序消息EncodingAESKey(消息加密密钥)");
        weixinConfig.setMchId("微信支付商户号");
        weixinConfig.setPayKey("微信支付 API 密钥");

        weixinConfig.setLcAppId("LeanCloud应用AppId");
        weixinConfig.setLcAppKey("LeanCloud应用AppKey");
        weixinConfig.setLcMasterKey("LeanCloud应用MasterKey");
        weixinConfig.setLcRestUrl("LeanCloud应用REST接口地址");

        weixinConfigDao.save(weixinConfig);

        log.info("=================== SystemConfig ===================");
        Arrays.asList(SysConfigCodeEnum.values()).parallelStream().forEach(code -> {
            String value = sysConfigDao.getByCode(code);
            log.info("{} = {}", code.name(), value);
        });

        return new BaseResponse(0, Constants.SUCCESS);
    }

}
