package com.zeroten.mproxy.thirdparty.wechat.msg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WechatImage {
    /**
     * 发送的图片的媒体ID，通过 新增素材接口 上传图片文件获得
     */
    @JSONField(name = "media_id")
    private String mediaId;

    public WechatImage(String mediaId) {
        this.mediaId = mediaId;
    }
}
