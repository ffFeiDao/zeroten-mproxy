package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    /**
     * 消息内容本身
     */
    @JSONField(name = "msg_content")
    private String msgContent;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容类型
     */
    @JSONField(name = "content_type")
    private String contentType;

    /**
     * JSON 格式的可选参数
     */
    private List<String> extras;
}
