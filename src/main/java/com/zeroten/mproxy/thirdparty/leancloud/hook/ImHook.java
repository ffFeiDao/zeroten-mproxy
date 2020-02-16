package com.zeroten.mproxy.thirdparty.leancloud.hook;

import cn.leancloud.IMHook;
import cn.leancloud.IMHookType;
import com.zeroten.mproxy.thirdparty.wechat.WechatTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ImHook {
    private static final Logger log = LogManager.getLogger(ImHook.class);

    @IMHook(type = IMHookType.messageSent)
    public static Map<String, Object> onMessageSent(Map<String, Object> params) {
        if (params.containsKey("content")) {
            String content = (String) params.get("content");
            if (StringUtils.isNotEmpty(content)) {
                String convId = getString(params, "convId");
                String fromPeer = getString(params, "fromPeer");

                log.info("deal reply message: {}#{}, {}", convId, fromPeer, content);
                
                WechatTool.dealReplyMessage(convId, fromPeer, content);
            }
        }
        return params;
    }

    private static String getString(Map<String, Object> params, String key) {
        if (params.containsKey(key)) {
            return params.get(key).toString();
        }
        return null;
    }
}
