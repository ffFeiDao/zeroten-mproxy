package com.zeroten.mproxy.thirdparty.wechat.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@Data
public class WechatBaseResponse {
    private String errcode;
    private String errmsg;

    public WechatBaseResponse(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public boolean isSuccess() {
        if ("0".equals(errcode) || StringUtils.isEmpty(errcode)) {
            return true;
        }
        return false;
    }
}
