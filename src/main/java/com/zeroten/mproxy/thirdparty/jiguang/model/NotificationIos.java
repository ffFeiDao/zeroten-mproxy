package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class NotificationIos {
    /**
     * 通知内容
     */
    private String alert;

    /**
     * 	通知提示声音或警告通知
     */
    private String sound = "default";

    /**
     * 应用角标
     */
    private Integer badge = 1;

    /**
     * 推送唤醒
     */
    @JSONField(name = "content-available")
    private Boolean contentAvailable = true;
}
