package com.zeroten.mproxy.util;

import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.enums.SysConfigCodeEnum;
import com.zeroten.mproxy.thirdparty.wechat.util.WechatPayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CheckUtils {
    private static final Logger log = LogManager.getLogger(CheckUtils.class);

    public static boolean isNotEmpty(String... strs) {
        for (String str : strs) {
            if (StringUtils.isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static String calcSign(SysConfigDao sysConfigDao, Map<String, String> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        sb.append(sysConfigDao.getByCode(SysConfigCodeEnum.API_SECRET, ""));
        for (String k : keyArray) {
            if (k.equals("sign") || data.get(k) == null) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append(data.get(k).trim());
        }

        String signSourceString = sb.toString();

        log.info("sign source string: {}", sb.toString());

        try {
            return WechatPayUtils.MD5(signSourceString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
