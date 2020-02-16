package com.zeroten.mproxy.thirdparty.jiguang;

import com.zeroten.mproxy.thirdparty.jiguang.enums.ClientTypeEnum;
import lombok.Data;

/**
 * 推送内容
 */
@Data
public class PushContent {
    private String title;

    private String content;

    private String clientId;

    private ClientTypeEnum clientType;
}
