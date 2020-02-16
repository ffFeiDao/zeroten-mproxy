package com.zeroten.mproxy.service.impl;

import com.alibaba.fastjson.JSON;
import com.zeroten.mproxy.dao.AccountDao;
import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.dao.WeixinConfigDao;
import com.zeroten.mproxy.dao.WeixinCustomMessageDao;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.entity.WeixinCustomMessage;
import com.zeroten.mproxy.enums.WxCustomMessageStatusEnum;
import com.zeroten.mproxy.service.MessageProxyService;
import com.zeroten.mproxy.thirdparty.jiguang.JGTool;
import com.zeroten.mproxy.thirdparty.jiguang.PushResponse;
import com.zeroten.mproxy.thirdparty.leancloud.LcTool;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcMessage;
import com.zeroten.mproxy.thirdparty.wechat.WechatHelper;
import com.zeroten.mproxy.thirdparty.wechat.WechatTool;
import com.zeroten.mproxy.thirdparty.wechat.msg.WechatMessage;
import com.zeroten.mproxy.tool.ImageUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProxyServiceImpl implements MessageProxyService {
    private static final Logger log = LogManager.getLogger(MessageProxyServiceImpl.class);

    @Autowired
    WeixinCustomMessageDao weixinCustomMessageDao;

    @Autowired
    WeixinConfigDao weixinConfigDao;

    @Autowired
    SysConfigDao sysConfigDao;

    @Autowired
    AccountDao accountDao;

    @Override
    public void deal(WeixinCustomMessage message) {
        log.info("deal wechat custom message: {},{},{},{}",
                message.getObjectId(),
                message.getFromUserName(),
                message.getToUserName(),
                message.getContent());

        message.setStatus(WxCustomMessageStatusEnum.SUCCESS.name());
        weixinCustomMessageDao.update(message, "status");

        String sessionFrom = message.getSessionFrom();

        if (StringUtils.isEmpty(sessionFrom)) {
            log.error("{} session from is empty", message.getToUserName());
            return;
        }

        WeixinConfig weixinConfig = weixinConfigDao.getByGhId(message.getToUserName());
        if (weixinConfig == null) {
            log.error("weixin config not exist {}", message.getToUserName());
        } else {
            ImageUpload imageUpload = new ImageUpload(weixinConfig);
            if (message.isImageMessage()) {
                String qnUrl = imageUpload.uploadImageToQiuNiu(message.getPicUrl(), message.getMediaId(), sysConfigDao);
                message.setQiniuUrl(qnUrl);
            } else if (message.isMiniProgramPage()) {
                String qnUrl = imageUpload.uploadImageToQiuNiu(message.getThumbUrl(), message.getThumbMediaId(), sysConfigDao);
                message.setQiniuUrl(qnUrl);
            }
            LcMessage lcMessage = LcMessage.parseFrom(message);
            if (lcMessage == null) {
                log.error("convert weixin message to leancloud message fail {}", JSON.toJSONString(message));
            } else {
                boolean sendSuccess = false;
                LcTool lcTool = new LcTool(sysConfigDao.getLcConfig());
                // leancloud im
                String convTitle = "客服消息";
                String convId = lcTool.createConversation(convTitle, message.getFromUserName(), sessionFrom);
                if (StringUtils.isNotEmpty(convId)) {
                    String msgId = lcTool.sendMessage(convId, message.getFromUserName(), lcMessage);
                    if (StringUtils.isNotEmpty(msgId)) {
                        sendSuccess = true;
                    }
                }
                if (sendSuccess) {
                    WechatTool.add(convId, convTitle, message.getFromUserName(), sessionFrom, weixinConfig);
                }
                if (!sendSuccess) {
                    new WechatHelper(weixinConfig).sendMessage(message.getFromUserName(), WechatMessage.createTextMessage("客服不在线"));
                }
                // jg push when lean cloud user not online
                if (!lcTool.checkOnline(sessionFrom).contains(sessionFrom)) {
                    PushResponse pushResponse = new JGTool(sysConfigDao.getPushConfig(), accountDao).push(sessionFrom, lcMessage.getPushTitle(), lcMessage.getPushContent());
                    if (pushResponse.isSuccess()) {
                        log.info("push app message to {},{}", sessionFrom, pushResponse.getMsgId());
                    } else {
                        log.error("push app message to {} error, {}", sessionFrom, pushResponse.getError());
                    }
                }
            }
        }
    }

}
