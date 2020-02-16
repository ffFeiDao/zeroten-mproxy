package com.zeroten.mproxy.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 微信客服消息
 */
@Data
public class WeixinCustomMessage extends BaseEntity {
    /**
     * 小程序的原始ID
     */
    private String toUserName;

    /**
     * 发送者的openid
     */
    private String fromUserName;

    /**
     * 消息创建时间
     */
    private Long createTime;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 图片链接（由系统生成）
     */
    private String picUrl;

    /**
     * 图片消息媒体id，可以调用[获取临时素材]((getTempMedia)接口拉取数据。
     */
    private String mediaId;

    /**
     * 事件类型
     */
    private String event;

    /**
     * 开发者在客服会话按钮设置的 session-from 属性
     */
    private String sessionFrom;

    /**
     * 消息id，64位整型
     */
    private Long msgId;

    /**
     * 标题
     */
    private String title;

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 小程序页面路径
     */
    private String pagePath;

    /**
     * 封面图片的临时cdn链接
     */
    private String thumbUrl;

    /**
     * 封面图片的临时素材id
     */
    private String thumbMediaId;

    /**
     * 七牛Url
     */
    private String qiniuUrl;

    /**
     * 状态
     */
    private String status;

    @JSONField(serialize = false)
    public boolean isValid() {
        return StringUtils.isNotEmpty(msgType) && StringUtils.isNotEmpty(fromUserName) && StringUtils.isNotEmpty(toUserName);
    }

    /**
     * 用户点击客服消息进入事件
     *
     * @return
     */
    @JSONField(serialize = false)
    public boolean isUserEnterEvent() {
        return "event".equals(msgType) && "user_enter_tempsession".equals(event);
    }

    @JSONField(serialize = false)
    public boolean isTextMessage() {
        return "text".equals(msgType);
    }

    @JSONField(serialize = false)
    public boolean isImageMessage() {
        return "image".equals(msgType);
    }

    @JSONField(serialize = false)
    public boolean isMiniProgramPage() {
        return "miniprogrampage".equals(msgType);
    }
}
