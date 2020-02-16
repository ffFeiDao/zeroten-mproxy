package com.zeroten.mproxy.thirdparty.jiguang;

import com.alibaba.fastjson.JSON;
import com.zeroten.mproxy.thirdparty.jiguang.enums.ClientTypeEnum;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;

public class JiGuangPushClientTest {
    PushConfig config = new PushConfig();

    {
        config.setKey("b9c95ec1de9fdae6d5ae6543");
        config.setSecret("ae293288a63579ddd94253d2");
//        config.setKey("671b89e3293d2ac93ee83054");
//        config.setSecret("3f25fe5a1de02ba474045f76");
    }

    @Test
    public void testPush() {
        PushContent content = new PushContent();
        content.setTitle("推送标题-" + new DateTime().toString("yyyy-MM-dd HH:mm:ss.SSS"));
        content.setContent("推送内容-" + new DateTime().toString("yyyy-MM-dd HH:mm:ss.SSS"));
        content.setClientId("171976fa8aef9b2876a");
        //content.setClientId("170976fa8aeb0034784");
        content.setClientType(ClientTypeEnum.IOS);
        PushResponse response = new JiGuangPushClient().setConfig(config).push(content);
        System.out.println(JSON.toJSONString(response));
    }
}