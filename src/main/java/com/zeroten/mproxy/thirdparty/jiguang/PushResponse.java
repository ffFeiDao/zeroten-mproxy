package com.zeroten.mproxy.thirdparty.jiguang;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@Data
public class PushResponse {
    private String error;

    private String msgId;

    public PushResponse(String msgId, String error) {
        this.msgId = msgId;
        this.error = error;
    }

    public boolean isSuccess() {
        return StringUtils.isEmpty(error);
    }
}
