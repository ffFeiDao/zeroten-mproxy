package com.zeroten.mproxy.thirdparty.leancloud.msg;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class LcTextMessage extends LcMessage {

    private String text;

    public LcTextMessage(String text) {
        this.text = text;
    }

    @Override
    public String getPushTitle() {
        return "文本消息";
    }

    @Override
    public String getPushContent() {
        return text;
    }

    @Override
    public Map toLcTextObject() {
        Map msg = Maps.newHashMap();
        msg.put("_lctype", -1);
        msg.put("_lctext", text);
        msg.put("_lcattrs", new Object());

        return msg;
    }

}
