package com.zeroten.mproxy.dao;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.SysConfig;
import com.zeroten.mproxy.enums.SysConfigCodeEnum;
import com.zeroten.mproxy.thirdparty.jiguang.PushConfig;
import com.zeroten.mproxy.thirdparty.leancloud.LcConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysConfigDao extends BaseDao<SysConfig> {

    private Map<SysConfigCodeEnum, String> valueCache = Maps.newConcurrentMap();

    public String getByCode(SysConfigCodeEnum code, String defaultValue) {
        if (valueCache.containsKey(code)) {
            return valueCache.get(code);
        }

        String value = defaultValue;

        Map<String, Object> where = Maps.newHashMap();
        where.put("code", code.name());

        List<SysConfig> list = list(SysConfig.class, where);
        if (list.size() == 1) {
            SysConfig sysConfig = list.get(0);
            if (StringUtils.isEmpty(sysConfig.getDescription())) {
                sysConfig.setDescription(code.getText());
                update(sysConfig, "description");
            }
            if (StringUtils.isNotEmpty(sysConfig.getValue())) {
                value = sysConfig.getValue();
                valueCache.put(code, value);
            }
        } else if (list.isEmpty()) {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode(code.name());
            sysConfig.setValue("");
            sysConfig.setDescription(code.getText());
            save(sysConfig);
        }

        return value;
    }

    public String getByCode(SysConfigCodeEnum code) {
        return getByCode(code, null);
    }

    public boolean isTrue(SysConfigCodeEnum code) {
        String value = getByCode(code);
        if ("true".equals(value) || "1".equals(value)) {
            return true;
        }
        return false;
    }

    public Boolean isTrueOrNull(SysConfigCodeEnum code) {
        String value = getByCode(code);
        if ("true".equals(value) || "1".equals(value)) {
            return true;
        } else if ("false".equals(value) || "0".equals(value)) {
            return false;
        }
        return null;
    }

    public LcConfig getLcConfig() {
        LcConfig lcConfig = new LcConfig();

        lcConfig.setAppId(getByCode(SysConfigCodeEnum.LC_IM_APPID));
        lcConfig.setAppKey(getByCode(SysConfigCodeEnum.LC_IM_APPKEY));
        lcConfig.setMasterKey(getByCode(SysConfigCodeEnum.LC_IM_MASTERKEY));
        lcConfig.setRestUrl(getByCode(SysConfigCodeEnum.LC_IM_REST_URL));

        return lcConfig;
    }

    public PushConfig getPushConfig() {
        PushConfig pushConfig = new PushConfig();

        pushConfig.setKey(getByCode(SysConfigCodeEnum.JG_KEY));
        pushConfig.setSecret(getByCode(SysConfigCodeEnum.JG_SECRET));
        pushConfig.setApnsProduction(isTrueOrNull(SysConfigCodeEnum.JG_APNS));

        return pushConfig;
    }

    public void clearCache() {
        valueCache.clear();
    }

}
