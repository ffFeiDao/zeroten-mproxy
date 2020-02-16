package com.zeroten.mproxy.entity;

import lombok.Data;

@Data
public class OrderForm extends BaseEntity {
    private String objectId;
    private String number;
    private Integer payMoney;
    private String orderOperatingRecord;
    private Integer orderState;
}
