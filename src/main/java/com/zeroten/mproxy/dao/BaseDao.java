package com.zeroten.mproxy.dao;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zeroten.mproxy.constant.Constants;
import com.zeroten.mproxy.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BaseDao<T extends BaseEntity> extends BaseRestDao<T> {
    protected List<T> list(Class<T> tClass, Map<String, Object> where) {
        AVQuery<AVObject> query = new AVQuery<>(getStorageName(tClass));
        if (where != null && !where.isEmpty()) {
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (key == null || val == null) {
                    continue;
                }
                query.whereEqualTo(key, val);
            }
        }
        List<AVObject> result = query.find();

        List<T> data = Lists.newArrayListWithCapacity(result.size());

        result.stream().forEach(row -> data.add(toObject(tClass, row)));

        return data;
    }

    protected T findOne(Class<T> tClass, Map<String, Object> where) {
        List<T> list = list(tClass, where);
        return list.size() == 1 ? list.get(0) : null;
    }

    private T toObject(Class<T> tClass, AVObject avObject) {
        if (avObject == null) {
            return null;
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(avObject.getServerData());
        return jsonObject.toJavaObject(tClass);
    }

    public T getById(Class<T> tClass, String objectId) {
        AVQuery<AVObject> query = new AVQuery<>(getStorageName(tClass));
        AVObject avObject = query.get(objectId);
        return toObject(tClass, avObject);
    }

    public String save(T entity) {
        AVObject avObject;
        if (StringUtils.isNotEmpty(entity.getObjectId())) {
            avObject = AVObject.createWithoutData(getStorageName(entity), entity.getObjectId());
        } else {
            avObject = new AVObject(getStorageName(entity));
        }
        fillAVObject(avObject, entity, null);

        avObject.save();

        return avObject.getObjectId();
    }

    public void update(T entity, String... fields) {
        if (StringUtils.isEmpty(entity.getObjectId())) {
            return;
        }
        AVObject avObject = AVObject.createWithoutData(getStorageName(entity), entity.getObjectId());
        fillAVObject(avObject, entity, fields);

        avObject.save();
    }

    public void deleteById(Class<T> tClass, String objectId) {
        AVObject avObject = AVObject.createWithoutData(getStorageName(tClass), objectId);
        avObject.delete();
    }

    private String getStorageName(T entity) {
        return getStorageName(entity.getClass());
    }

    private Map<String, Object> toMap(T entity) {
        return (JSONObject) JSON.toJSON(entity);
    }

    private AVObject fillAVObject(AVObject avObject, T entity, String... fields) {
        List<String> fieldList = fields == null ? Lists.newArrayListWithCapacity(0) : Arrays.asList(fields);
        for (Map.Entry<String, Object> entry : toMap(entity).entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            if (Constants.OBJECT_ID.equals(field) || value == null) {
                continue;
            }
            if (!fieldList.isEmpty() && !fieldList.contains(field)) {
                continue;
            }
            avObject.put(entry.getKey(), value);
        }
        return avObject;
    }
}
