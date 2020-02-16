package com.zeroten.mproxy.thirdparty.jiguang.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PushResponse {
    private Integer sendno;

    private String msgId;

    private Error error;

    public boolean isSuccess() {
        return error == null && StringUtils.isNotEmpty(msgId);
    }

    public String getErrorMessage() {
        if (error != null) {
            return String.format("%d,%s", error.getCode(), error.getMessage());
        }

        return "";
    }
}
