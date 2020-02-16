package com.zeroten.mproxy.thirdparty.wechat.pay.bean;

import com.zeroten.mproxy.thirdparty.wechat.WechatPayConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WechatPayOrderQueryResponse extends WechatPayBaseResponse {

    private String deviceInfo;
    private String openid;
    private String isSubscribe;
    private String tradeType;
    private String tradeState;
    private String bankType;
    private Integer totalFee;
    private Integer settlementTotalFee;
    private String feeType;
    private Integer cashFee;
    private String cashFeeType;
    private Integer couponFee;
    private Integer couponCount;
    private String transactionId;
    private String outTradeNo;
    private String attach;
    private String timeEnd;
    private String tradeStateDesc;

    public WechatPayOrderQueryResponse(String returnCode, String returnMsg) {
        super(returnCode, returnMsg);
    }

    public boolean isPaySuccess() {
        return isBusinessSuccess() && WechatPayConstants.SUCCESS.equals(tradeState);
    }
}
