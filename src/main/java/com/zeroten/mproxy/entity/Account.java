package com.zeroten.mproxy.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Account extends BaseEntity {
    private String appId;
    private String appKey;
    private String brandName;
    private String clerkState;
    private String clientId;
    private String clientType;
    private String registrationId;
    private String storeName;
    private List<String> tags;
    private String telephone;
    private String url;
    private Date createAt;
    private Date updatedAt;
}
