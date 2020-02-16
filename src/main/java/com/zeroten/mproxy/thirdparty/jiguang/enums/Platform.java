package com.zeroten.mproxy.thirdparty.jiguang.enums;

public enum Platform {

    all, android, ios, winphone;

    public static Platform of(ClientTypeEnum clientType) {
        if (clientType == null) {
            return all;
        }
        if (clientType.isAndroid()) {
            return android;
        }
        if (clientType.isIOS()) {
            return ios;
        }
        return all;
    }
}
