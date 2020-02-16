package com.zeroten.mproxy.constant;

public class UrlMapping {

    /**
     * 用户
     */
    public static final String USER = "/user";
    public static final String USER_LOGIN = USER + "/login";
    public static final String USER_LOGOUT = USER + "/logout";
    public static final String USER_INFO = USER + "/info";

    public static final String ACCOUNT_INFO = "/account/info";
    public static final String ACCOUNT_INFO_UPDATE = "/account/info-update";

    /**
     * 订单
     */
    public static final String ORDER_QUERY = "/order/query";

    /**
     * 微信
     */
    public static final String WECHAT = "/wechat";
    public static final String WECHAT_MESSAGE_RECEIVE = WECHAT + "/message-receive";
    public static final String WECHAT_PAY_RESULT_NOTIFY = WECHAT + "/pay-result-notify";
    public static final String WECHAT_GET_TOKEN = WECHAT + "/get-token";

    /**
     * 极光推送
     */
    public static final String USER_PUSH_BIND = USER + "/push-bind";
    public static final String USER_PUSH_UNBIND = USER + "/push-unbind";

    /**
     * im消息
     */
    public static final String IM = "/im";
    public static final String IM_MSG_HOOK = IM + "/msg-hook";

    public static final String RESET = "/reset";

    public static final String TEST = "/test";

}
