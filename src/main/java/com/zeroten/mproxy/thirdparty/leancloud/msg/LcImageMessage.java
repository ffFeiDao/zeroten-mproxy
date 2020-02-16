package com.zeroten.mproxy.thirdparty.leancloud.msg;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class LcImageMessage extends LcMessage {
    private String url;
    private String objId;
    private String name;
    private String format;

    @Override
    public String getPushTitle() {
        return "您收到图片";
    }

    @Override
    public String getPushContent() {
        return "您收到客户发送的图片";
    }

    @Override
    public Map toLcTextObject() {
        Map msg = Maps.newHashMap();
        msg.put("_lctype", -2);
        msg.put("_lcattrs", new Object());

        Map<String, Object> _lcfile = Maps.newHashMap();
        _lcfile.put("url", url);
        _lcfile.put("objId", StringUtils.defaultIfEmpty(objId, ""));

        Map<String, Object> metaData = Maps.newHashMap();
        metaData.put("name", name);
        metaData.put("format", format);
        _lcfile.put("metaData", metaData);

        msg.put("_lcfile", _lcfile);

        return msg;
    }
}
