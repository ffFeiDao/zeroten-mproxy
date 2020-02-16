package com.zeroten.mproxy.thirdparty.leancloud;

import cn.leancloud.AVUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.thirdparty.leancloud.msg.LcMessage;
import com.zeroten.mproxy.util.BaseClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class LcTool {
    private static final Logger log = LogManager.getLogger(LcTool.class);

    LcConfig config;

    public LcTool(LcConfig config) {
        this.config = config;
    }

    public String createConversation(String conversationTitle, String... clientIds) {
        String url = String.format("%s/1.2/rtm/conversations", config.getRestUrl());

        Map<String, Object> params = Maps.newHashMap();
        params.put("name", conversationTitle);
        params.put("m", clientIds);
        params.put("unique", true);

        String paramJSONString = JSON.toJSONString(params);

        try {
            String respString = new BaseClient().post(url, getHeader(), paramJSONString);
            log.info("create leancloud conversation: {}, {}", paramJSONString, respString);
            return JSON.parseObject(respString).getString("objectId");
        } catch (Exception e) {
            log.error("create leancloud conversation error: {}", paramJSONString, e);
        }

        return null;
    }

    public String sendMessage(String convId, String fromClient, LcMessage lcMessage) {
        String url = String.format("%s/1.2/rtm/conversations/%s/messages", config.getRestUrl(), convId);
        String messageJSONString = lcMessage.toLcText(fromClient);
        try {
            messageJSONString = messageJSONString.replace("\\/", "/");
            String respString = new BaseClient().post(url, getHeader(), messageJSONString);
            log.info("send text message: {}, {}", messageJSONString, respString);
            JSONObject jo = JSON.parseObject(respString);
            return jo.containsKey("result") ? jo.getJSONObject("result").getString("msg-id") : null;
        } catch (Exception e) {
            log.error("send text message error: {}", messageJSONString, e);
        }

        return null;
    }

    public List<String> checkOnline(String... clientIds) {
        String url = String.format("%s/1.2/rtm/clients/check-online", config.getRestUrl());
        if (clientIds != null && clientIds.length > 0) {
            Map<String, Object> paramObject = Maps.newHashMap();
            paramObject.put("client_ids", clientIds);
            String paramJSONString = JSON.toJSONString(paramObject);
            try {
                String respString = new BaseClient().post(url, getHeader(), paramJSONString);
                log.info("check leancloud user online: {}, {}", paramJSONString, respString);
                JSONObject jo = JSON.parseObject(respString);
                return jo.containsKey("results") ? jo.getJSONArray("results").toJavaList(String.class) : Lists.newArrayList();
            } catch (Exception e) {
                log.error("check leancloud user online error: {}", paramJSONString, e);
            }
        }

        return Lists.newArrayList();
    }

    private Map<String, String> getHeader() {
        Map<String, String> header = Maps.newHashMap();
        header.put("X-LC-Id", config.getAppId());
        header.put("X-LC-Key", String.format("%s,master", config.getMasterKey()));
        return header;
    }

    public static AVUser getLoginUser(HttpServletRequest request) {
        AVUser loginUser = AVUser.getCurrentUser();

        if (loginUser == null || !loginUser.isAuthenticated()) {
            String sessionToken = request.getHeader(Constants.LC_SESSION_TOKEN);
            if (StringUtils.isNotEmpty(sessionToken)) {
                loginUser = AVUser.becomeWithSessionToken(sessionToken);
            }
        }

        return loginUser;
    }
}
