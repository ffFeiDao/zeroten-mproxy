package com.zeroten.mproxy.controller;

import com.zeroten.mproxy.constant.UrlMapping;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.WechatMessageService;
import com.zeroten.mproxy.util.WebUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping
public class WechatController {
    private static final Logger log = LogManager.getLogger(WechatController.class);

    @Autowired
    WechatMessageService wechatMessageService;

    @GetMapping(UrlMapping.WECHAT_MESSAGE_RECEIVE)
    public String messageReceiveGet(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        return echostr;
    }

    @PostMapping(UrlMapping.WECHAT_MESSAGE_RECEIVE)
    public String messageReceivePost(HttpServletRequest request) {
        String data = WebUtils.getStreamAsString(request, "UTF-8");
        Map<String, String> params = WebUtils.getParameters(request);
        String contentType = request.getContentType();

        return wechatMessageService.dealReceiveMessage(contentType, params, data);
    }

    @PostMapping(UrlMapping.WECHAT_PAY_RESULT_NOTIFY)
    public String payResultNotify(HttpServletRequest request, HttpServletResponse response) {
        String data = WebUtils.getStreamAsString(request, "UTF-8");
        Map<String, String> params = WebUtils.getParameters(request);
        String contentType = request.getContentType();

        response.setContentType("application/xml");

        return wechatMessageService.payResultNotify(contentType, params, data);
    }

    @GetMapping(UrlMapping.WECHAT_GET_TOKEN)
    public BaseResponse getToken(@RequestParam("appId") String appId,
                                 @RequestParam("key") String key,
                                 @RequestParam("timestamp") String timestamp,
                                 @RequestParam("sign") String sign,
                                 HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization,content-type");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        return wechatMessageService.getAccessToken(appId, key, timestamp, sign);
    }

    @GetMapping(UrlMapping.TEST)
    public void test() {
        wechatMessageService.test();
    }

}
