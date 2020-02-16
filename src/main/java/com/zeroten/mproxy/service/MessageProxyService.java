package com.zeroten.mproxy.service;

import com.zeroten.mproxy.entity.WeixinCustomMessage;

public interface MessageProxyService {

    void deal(WeixinCustomMessage message);

}
