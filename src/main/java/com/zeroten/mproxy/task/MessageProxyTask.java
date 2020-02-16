package com.zeroten.mproxy.task;

import com.alibaba.fastjson.JSON;
import com.zeroten.mproxy.entity.WeixinCustomMessage;
import com.zeroten.mproxy.service.MessageProxyService;
import com.zeroten.mproxy.tool.ValueBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProxyTask {
    private static final Logger log = LogManager.getLogger(MessageProxyTask.class);

    private static ValueBox<WeixinCustomMessage> wechatMessages = new ValueBox<>();

    @Autowired
    MessageProxyService messageProxyService;

    {
        new Thread(() -> {
            while (true) {
                try {
                    messageProxyService.deal(wechatMessages.get());
                } catch (Exception e) {
                    log.error("deal wechat custom message error", e);
                }
            }
        }).start();

    }

    public void add(WeixinCustomMessage wxCustomMessage) {
        try {
            wechatMessages.add(wxCustomMessage);
        } catch (InterruptedException e) {
            log.error("Add wechat custom message to queue fail: {}", JSON.toJSONString(wxCustomMessage));
        }
    }

}
