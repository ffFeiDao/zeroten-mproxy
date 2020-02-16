package com.zeroten.mproxy.thirdparty.wechat.msg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WechatLink {
    /**
     * 消息标题
     */
    private String title;

    /**
     * 图文链接消息
     */
    private String description;

    /**
     * 图文链接消息被点击后跳转的链接
     */
    private String url;

    /**
     * 图文链接消息的图片链接，支持 JPG、PNG 格式，较好的效果为大图 640 X 320，小图 80 X 80
     */
    @JSONField(name = "thumb_url")
    private String thumbUrl;
}
