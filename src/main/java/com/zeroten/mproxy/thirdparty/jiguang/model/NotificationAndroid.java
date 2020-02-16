package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class NotificationAndroid {
    /**
     * 通知内容
     */
    private String alert;

    /**
     * 	通知标题
     */
    private String title;

    /**
     * 通知栏样式 ID
     */
    @JSONField(name = "builder_id")
    private Integer builderId;

    /**
     * android通知channel_id
     */
    @JSONField(name = "channel_id")
    private String channelId;

    /**
     * 通知栏展示优先级：默认为 0，范围为 -2～2
     */
    private Integer priority;

    @JSONField(name = "alert_type")
    private Integer alertType = -1;
}
