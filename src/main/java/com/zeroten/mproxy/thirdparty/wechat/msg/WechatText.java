package com.zeroten.mproxy.thirdparty.wechat.msg;

import lombok.Data;

@Data
public class WechatText {
    /**
     * 文本消息内容
     */
    private String content;

    public WechatText(String content) {
        this.content = content;
    }
}
