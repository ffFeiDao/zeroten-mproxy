package com.zeroten.mproxy.thirdparty.jiguang.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class Audience {
    /**
     * 标签，一次推送最多 20 个
     */
    private List<String> tag;

    /**
     * 标签，多个标签之间是 AND 关系，即取交集
     */
    @JSONField(name = "tag_and")
    private List<String> tagAnd;

    /**
     * 标签，多个标签之间，先取多标签的并集，再对该结果取补集
     */
    @JSONField(name = "tag_not")
    private List<String> tagNot;

    /**
     * 别名，一次推送最多 1000 个
     */
    private List<String> alias;

    /**
     * 注册ID，一次推送最多 1000 个
     */
    @JSONField(name = "registration_id")
    private List<String> registrationId;

    /**
     * 用户分群 ID，目前限制是一次只能推送一个
     */
    private List<String> segment;

    /**
     * A/B Test ID，目前限制一次只能推送一个
     */
    private String abtest;
}
