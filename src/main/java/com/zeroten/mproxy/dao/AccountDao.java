package com.zeroten.mproxy.dao;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountDao extends BaseDao<Account> {
    private Map<String, Account> valueCache = Maps.newConcurrentMap();

    public List<Account> listByTelephone(String telephone) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("telephone", telephone);

        return list(Account.class, where);
    }

    public Account getByClientId(String clientId) {
        Account account = valueCache.get(clientId);
        if (account == null) {
            Map<String, Object> where = Maps.newHashMap();
            where.put("clientId", clientId);
            account = findOne(Account.class, where);
            if (account != null) {
                valueCache.put(clientId, account);
            }
        }
        return account;
    }

    public String update(String clientId, String clientType, String registrationId) {
        Account account = valueCache.get(clientId);
        if (account == null) {
            account = getByClientId(clientId);
        }
        if (account != null) {
            account.setClientType(clientType);
            account.setRegistrationId(registrationId);
            update(account, "clientType", "registrationId");
            return account.getObjectId();
        }
        return null;
    }

    public String update(String clientId, Integer clerkState) {
        Account account = valueCache.get(clientId);
        if (account == null) {
            account = getByClientId(clientId);
        }
        if (account != null) {
            account.setClerkState(clerkState == null || clerkState == 0 ? "离职" : "在职");
            update(account, "clerkState");
            return account.getObjectId();
        }
        return null;
    }

    public void clearCache() {
        valueCache.clear();
    }

}
