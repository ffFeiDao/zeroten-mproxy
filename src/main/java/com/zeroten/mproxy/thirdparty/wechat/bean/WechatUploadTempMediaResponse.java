package com.zeroten.mproxy.thirdparty.wechat.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@Data
public class WechatUploadTempMediaResponse extends WechatBaseResponse {

    public WechatUploadTempMediaResponse(String errcode, String errmsg) {
        super(errcode, errmsg);
    }

    /**
     * 文件类型
     */

    private String type;

    /**
     * 媒体文件上传后，获取标识，3天内有效。
     */
    private String mediaId;

    /**
     * 媒体文件上传时间戳
     */
    private Long createdAt;

    @Override
    public boolean isSuccess() {
        if (!super.isSuccess()) {
            return false;
        }
        if (StringUtils.isEmpty(mediaId)) {
            return false;
        }
        return true;
    }

}
