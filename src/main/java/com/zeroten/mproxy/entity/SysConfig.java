package com.zeroten.mproxy.entity;

import lombok.Data;

/**
 * 系统配置表
 */
@Data
public class SysConfig extends BaseEntity {
    private String code;
    private String value;
    private String description;
}
