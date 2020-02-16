package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class PushOptions {

    @JSONField(name = "apns_production")
    private Boolean apnsProduction;

}
