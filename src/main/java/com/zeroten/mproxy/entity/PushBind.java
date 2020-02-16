package com.zeroten.mproxy.entity;

import lombok.Data;

@Deprecated
@Data
public class PushBind extends BaseEntity {
    private String clientType;
    private String clientId;
    private String regId;
}
