package com.zeroten.mproxy.thirdparty.wechat;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcMessage;
import com.zeroten.mproxy.thirdparty.wechat.msg.WechatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class WechatTool {
    private static final Logger log = LogManager.getLogger(WechatTool.class);

    private static Map<String, Conv> cache = Maps.newConcurrentMap();

    public static void add(String convId, String convTitle, String openid, String lcClientId, WeixinConfig weixinConfig) {
        String key = key(convId, lcClientId);

        log.info("cache conv {} {}", key, convTitle);

        cache.put(key, new Conv(convId, convTitle, openid, lcClientId, weixinConfig));
    }

    public static void dealReplyMessage(String convId, String lcClientId, String content) {
        LcMessage lcMessage = LcMessage.parseFrom(content);
        if (lcMessage == null) {
            log.info("lean message format error {}", content);
            return;
        }
        String key = key(convId, lcClientId);

        if (!cache.containsKey(key)) {
            log.info("conv not exist {}", key);
            return;
        }
        Conv conv = cache.get(key);

        log.info("find conv {}", key);

        new WechatHelper(conv.weixinConfig).sendMessage(conv.openid, WechatMessage.parseFrom(lcMessage));
    }

    private static String key(String convId, String lcClientId) {
        return String.format("%s#%s", convId, lcClientId);
    }

    public static class Conv {
        public String convId;
        public String convTitle;
        public String openid;
        public String lcCliendId;
        public WeixinConfig weixinConfig;

        public Conv(String convId, String convTitle, String openid, String lcCliendId, WeixinConfig weixinConfig) {
            this.convId = convId;
            this.convTitle = convTitle;
            this.openid = openid;
            this.lcCliendId = lcCliendId;
            this.weixinConfig = weixinConfig;
        }
    }
}
