package com.zeroten.mproxy.thirdparty.wechat.msg;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WechatMiniProgramPage {
    /**
     * 消息标题
     */
    private String title;

    /**
     * 小程序的页面路径，跟app.json对齐，支持参数，比如pages/index/index?foo=bar
     */
    private String pagepath;

    /**
     * 小程序消息卡片的封面， image 类型的 media_id，通过 新增素材接口 上传图片文件获得，建议大小为 520*416
     */
    @JSONField(name = "thumb_media_id")
    private String thumbMediaId;
}
