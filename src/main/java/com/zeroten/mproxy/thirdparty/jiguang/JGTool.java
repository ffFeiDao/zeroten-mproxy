package com.zeroten.mproxy.thirdparty.jiguang;

import com.zeroten.mproxy.dao.AccountDao;
import com.zeroten.mproxy.entity.Account;
import com.zeroten.mproxy.thirdparty.jiguang.enums.ClientTypeEnum;
import org.apache.commons.lang3.StringUtils;

public class JGTool {
    JiGuangPushClient client;
    AccountDao dao;

    public JGTool(PushConfig config, AccountDao dao) {
        this.client = new JiGuangPushClient().setConfig(config);
        this.dao = dao;
    }

    public PushResponse push(String clientId, String title, String content) {
        Account account = dao.getByClientId(clientId);
        if (account == null) {
            new PushResponse(null, "Client id not exist: " + clientId);
        }
        if (StringUtils.isEmpty(account.getClientType()) || StringUtils.isEmpty(account.getRegistrationId())) {
            new PushResponse(null, "Client not bind jiguang RegId: " + clientId);
        }
        ClientTypeEnum clientType = ClientTypeEnum.of(account.getClientType());
        if (clientType == null) {
            new PushResponse(null, "Can't recognize client type: " + account.getClientType());
        }
        PushContent pushContent = new PushContent();
        pushContent.setClientType(clientType);
        pushContent.setClientId(account.getRegistrationId());
        pushContent.setTitle(title);
        pushContent.setContent(content);

        return client.push(pushContent);
    }
}
