package com.zeroten.mproxy.thirdparty.wechat.msg;

import com.alibaba.fastjson.JSON;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcImageMessage;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcMessage;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcProductMessage;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcTextMessage;
import com.zeroten.mproxy.thirdparty.wechat.WechatHelper;
import com.zeroten.mproxy.thirdparty.wechat.bean.WechatUploadTempMediaResponse;
import lombok.Data;

@Data
public class WechatMessage {
    /**
     * 用户的 OpenID
     */
    private String touser;

    /**
     * 消息类型
     */
    private WechatMsgType msgtype;

    /**
     * 文本消息，msgtype="text" 时必填
     */
    private WechatText text;

    /**
     * 图片消息，msgtype="image" 时必填
     */
    private WechatImage image;

    /**
     * 图片消息，msgtype="link" 时必填
     */
    private WechatLink link;

    /**
     * 小程序卡片，msgtype="miniprogrampage" 时必填
     */
    private WechatMiniProgramPage miniprogrampage;

    public String toJSONString(String touser, WechatHelper wechatHelper) {
        this.touser = touser;
        if (msgtype == WechatMsgType.image && image != null && isUrl(image.getMediaId())) {
            WechatUploadTempMediaResponse response = wechatHelper.uploadTempMedia(image.getMediaId());
            if (response.isSuccess()) {
                image.setMediaId(response.getMediaId());
            }
        } else if (msgtype == WechatMsgType.miniprogrampage && miniprogrampage != null && isUrl(miniprogrampage.getThumbMediaId())) {
            WechatUploadTempMediaResponse response = wechatHelper.uploadTempMedia(miniprogrampage.getThumbMediaId());
            if (response.isSuccess()) {
                miniprogrampage.setThumbMediaId(response.getMediaId());
            }
        } else if (msgtype == WechatMsgType.link && link != null && isUrl(link.getThumbUrl())) {
            WechatUploadTempMediaResponse response = wechatHelper.uploadTempMedia(link.getThumbUrl());
            if (response.isSuccess()) {
                link.setThumbUrl(response.getMediaId());
            }
        }
        return JSON.toJSONString(this);
    }


    private boolean isUrl(String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("https://") || url.startsWith("http://");
    }

    public static WechatMessage parseFrom(LcMessage lcMessage) {
        if (lcMessage instanceof LcTextMessage) {
            LcTextMessage lcTextMessage = (LcTextMessage) lcMessage;

            WechatMessage wechatMessage = new WechatMessage();
            wechatMessage.setMsgtype(WechatMsgType.text);
            wechatMessage.setText(new WechatText(lcTextMessage.getText()));

            return wechatMessage;
        }

        if (lcMessage instanceof LcImageMessage) {
            LcImageMessage lcImageMessage = (LcImageMessage) lcMessage;

            WechatMessage wechatMessage = new WechatMessage();
            wechatMessage.setMsgtype(WechatMsgType.image);
            wechatMessage.setImage(new WechatImage(lcImageMessage.getUrl()));

            return wechatMessage;
        }

        if (lcMessage instanceof LcProductMessage) {
            LcProductMessage lcProductMessage = (LcProductMessage) lcMessage;

            WechatMessage wechatMessage = new WechatMessage();
            wechatMessage.setMsgtype(WechatMsgType.miniprogrampage);

            WechatMiniProgramPage miniProgramPage = new WechatMiniProgramPage();
            miniProgramPage.setTitle(lcProductMessage.getTitle());
            miniProgramPage.setPagepath(lcProductMessage.getPagePath());
            miniProgramPage.setThumbMediaId(lcProductMessage.getUrl());
            wechatMessage.setMiniprogrampage(miniProgramPage);

            return wechatMessage;
        }

        return null;
    }

    public static WechatMessage createTextMessage(String text) {
        WechatMessage wechatMessage = new WechatMessage();
        wechatMessage.setMsgtype(WechatMsgType.text);
        wechatMessage.setText(new WechatText(text));
        return wechatMessage;
    }

}
