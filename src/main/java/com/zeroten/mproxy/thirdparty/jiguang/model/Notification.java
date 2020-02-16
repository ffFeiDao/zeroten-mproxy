package com.zeroten.mproxy.thirdparty.jiguang.model;

import lombok.Data;

@Data
public class Notification {
    private String alert;

    NotificationAndroid android;

    NotificationIos ios;

    public Notification(NotificationAndroid android) {
        this.android = android;
    }

    public Notification(NotificationIos ios) {
        this.ios = ios;
    }

    public Notification(NotificationAndroid android, NotificationIos ios) {
        this.android = android;
        this.ios = ios;
    }

}
