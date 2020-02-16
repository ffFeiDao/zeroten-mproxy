package com.zeroten.mproxy.util;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebUtils {
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String val = request.getHeader(key);
            headers.put(key, val);
        }

        return headers;
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String val = request.getParameter(key);
            params.put(key, val);
        }

        return params;
    }

    public static String getStreamAsString(HttpServletRequest request, String charset) {
        try {
            return getStreamAsString(request.getInputStream(), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getStreamAsString(InputStream stream, String charset) {
        try {
            return IOUtils.toString(stream, charset);
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return "";
    }
}
