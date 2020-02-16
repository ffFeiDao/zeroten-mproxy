package com.zeroten.mproxy.tool;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zeroten.mproxy.dao.SysConfigDao;
import com.zeroten.mproxy.entity.WeixinConfig;
import com.zeroten.mproxy.entity.WeixinCustomMessage;
import com.zeroten.mproxy.enums.SysConfigCodeEnum;
import com.zeroten.mproxy.thirdparty.wechat.WechatHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUpload {
    private static final Logger log = LogManager.getLogger(ImageUpload.class);

    WeixinConfig config;

    public ImageUpload(WeixinConfig config) {
        this.config = config;
    }

    public String uploadImageToQiuNiu(WeixinCustomMessage wxMsg, SysConfigDao sysConfigDao) {
        if (wxMsg.isImageMessage()) {
            String imageUrl = wxMsg.getPicUrl();
            if (StringUtils.isEmpty(imageUrl) && StringUtils.isNotEmpty(wxMsg.getMediaId())) {
                imageUrl = new WechatHelper(config).getMediaDownUrl(wxMsg.getMediaId());
            }
            if (StringUtils.isNotEmpty(imageUrl)) {
                return uploadUrlToQiNiu(imageUrl, wxMsg.getMediaId(), sysConfigDao);
            }
        }

        return null;
    }

    public String uploadImageToQiuNiu(String url, String key, SysConfigDao sysConfigDao) {
        if (StringUtils.isNotEmpty(url)) {
            return uploadUrlToQiNiu(url, key, sysConfigDao);
        }

        return null;
    }

    private String uploadUrlToQiNiu(String url, String key, SysConfigDao sysConfigDao) {
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);

        String accessKey = sysConfigDao.getByCode(SysConfigCodeEnum.QINIU_ACCESSKEY);
        String secretKey = sysConfigDao.getByCode(SysConfigCodeEnum.QINIU_SECRETKEY);
        String bucket = sysConfigDao.getByCode(SysConfigCodeEnum.QINIU_BUCKET);
        String baseUrl = sysConfigDao.getByCode(SysConfigCodeEnum.QINIU_URL);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        InputStream is = null;
        try {
            is = getUrlInputStream(url);
            Response response = uploadManager.put(is, key, upToken, null, null);
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return String.format("%s/%s", baseUrl, putRet.key);
        } catch (Exception e) {
            log.error("upload image to qiniu error: {}", url, e);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }

        return null;
    }

    private InputStream getUrlInputStream(String url) throws Exception {
        URL oUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) oUrl.openConnection();
        connection.setConnectTimeout(6000);
        return connection.getInputStream();
    }
}
