package com.zeroten.mproxy.thirdparty.jiguang.enums;

/**
 * 客户端类型
 */
public enum ClientTypeEnum {
    ALL, ANDROID, IOS;

    public boolean isAll() {
        return ALL.equals(this);
    }

    public boolean isAndroid() {
        return ANDROID.equals(this);
    }

    public boolean isIOS() {
        return IOS.equals(this);
    }

    public static ClientTypeEnum of(String code) {
        try {
            return valueOf(code.toUpperCase());
        } catch (Exception e) {
        }
        return null;
    }
}
