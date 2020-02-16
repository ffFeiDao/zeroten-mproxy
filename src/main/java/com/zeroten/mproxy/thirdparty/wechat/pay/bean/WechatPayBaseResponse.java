package com.zeroten.mproxy.thirdparty.wechat.pay.bean;

import com.zeroten.mproxy.thirdparty.wechat.WechatPayConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WechatPayBaseResponse {
    private String returnCode;
    private String returnMsg;

    // 以下字段在return_code为SUCCESS的时候有返回
    private String appid;
    private String mchId;
    private String nonceStr;
    private String sign;
    private String resultCode;
    private String errCode;
    private String errCodeDes;

    public WechatPayBaseResponse(String returnCode, String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }

    public boolean isSuccess() {
        return WechatPayConstants.SUCCESS.equals(returnCode);
    }

    public boolean isBusinessSuccess() {
        return isSuccess() && WechatPayConstants.SUCCESS.equals(resultCode);
    }
}
