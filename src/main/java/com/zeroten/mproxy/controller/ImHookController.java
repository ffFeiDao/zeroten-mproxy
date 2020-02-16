package com.zeroten.mproxy.controller;

import com.alibaba.fastjson.JSON;
import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.constant.UrlMapping;
import com.zeroten.mproxy.util.WebUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping
public class ImHookController {
    private static final Logger log = LogManager.getLogger(ImHookController.class);

    @PostMapping(UrlMapping.IM_MSG_HOOK)
    public String imMsgHook(HttpServletRequest request) {
        String data = WebUtils.getStreamAsString(request, "UTF-8");
        Map<String, String> params = WebUtils.getParameters(request);
        String contentType = request.getContentType();

        log.info("im message hook：{}, {}, {}", contentType, JSON.toJSONString(params), data);

        return Constants.SUCCESS;
    }

}
