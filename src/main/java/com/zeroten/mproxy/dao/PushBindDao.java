package com.zeroten.mproxy.dao;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.PushBind;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Deprecated
@Service
public class PushBindDao extends BaseDao<PushBind> {
    private Map<String, PushBind> valueCache = Maps.newConcurrentMap();

    public List<PushBind> listByClientId(String clientId) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("clientId", clientId);

        return list(PushBind.class, where);
    }

    public PushBind getByClientId(String clientId) {
        PushBind pushBind = valueCache.get(clientId);
        if (pushBind == null) {
            List<PushBind> list = listByClientId(clientId);
            if (!list.isEmpty()) {
                pushBind = list.get(0);
                valueCache.put(clientId, pushBind);
            }
        }
        return pushBind;
    }

    public String save(PushBind pushBind) {
        String id = super.save(pushBind);
        valueCache.remove(pushBind.getClientId());
        return id;
    }

}
