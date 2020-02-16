package com.zeroten.mproxy.util;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseClient {
    protected OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(600, TimeUnit.SECONDS)
            .build();

    public String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return execute("GET", url, headers, params);
    }

    public String get(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, null);
    }

    public String get(String url) throws IOException {
        return get(url, null, null);
    }

    public String delete(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        return execute("DELETE", url, headers, params);
    }

    public String delete(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, null);
    }

    public String delete(String url) throws IOException {
        return get(url, null, null);
    }

    public String post(String url, Map<String, String> headers, Map<String, String> bodyParams) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        FormBody.Builder body = new FormBody.Builder();
        if (bodyParams != null && bodyParams.size() > 0) {
            for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                body.add(entry.getKey(), entry.getValue());
            }
        }

        Response response = client.newCall(builder.post(body.build()).build()).execute();
        return response.body().string();
    }

    public String post(String url, Map<String, String> headers, String bodyJsonString) throws IOException {
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        RequestBody body = RequestBody.create(json, bodyJsonString);

        Response response = client.newCall(builder.post(body).build()).execute();
        return response.body().string();
    }

    public String upload(String url, Map<String, String> headers, Map<String, String> params, Map<String, Object> files) throws IOException {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                bodyBuilder.addFormDataPart(param.getKey(), param.getValue());
            }
        }
        for (Map.Entry<String, Object> fileParam : files.entrySet()) {
            RequestBody fileBody = null;
            Object fileObject = fileParam.getValue();
            if (fileObject instanceof byte[]) {
                fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), (byte[]) fileObject);
            } else if (fileObject instanceof File) {
                fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), (File) fileObject);
            } else if (fileObject instanceof String) {
                fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(fileObject.toString()));
            }
            if (fileBody != null) {
                bodyBuilder.addFormDataPart(fileParam.getKey(), "1.jpg", fileBody);
            }
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Response response = client.newCall(builder.post(bodyBuilder.build()).build()).execute();
        return response.body().string();
    }

    public byte[] getData(String url) throws IOException {
        Response response = _execute("GET", url, null, null);
        return response.body().bytes();
    }

    public String put(String url, Map<String, String> headers, String jsonData) throws IOException {
        MediaType json = MediaType.parse("application/json");

        Request.Builder builder = new Request.Builder().url(url);

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        RequestBody body = RequestBody.create(json, jsonData);

        Response response = client.newCall(builder.put(body).build()).execute();
        return response.body().string();
    }

    private String execute(String method, String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Response response = _execute(method, url, headers, params);
        return response.body().string();
    }

    private Response _execute(String method, String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue())) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if ("DELETE".equals(method)) {
            builder = builder.delete();
        }

        return client.newCall(builder.build()).execute();
    }

}