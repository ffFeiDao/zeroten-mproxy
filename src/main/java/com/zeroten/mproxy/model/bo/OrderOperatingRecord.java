package com.zeroten.mproxy.model.bo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.List;

@NoArgsConstructor
@Data
public class OrderOperatingRecord {
    private Object operationTime;
    private String operatorNumber;
    private String operatorClass;
    private Integer status;
    private String name;


//    @NoArgsConstructor
//    @Data
//    public static class OperationTime {
//        @JSONField(name = "__type")
//        private String type;
//        private String iso;
//
//        public OperationTime(String type, String iso) {
//            this.type = type;
//            this.iso = iso;
//        }
//    }

    public OrderOperatingRecord(String operatorClass, Integer status) {
//        this.operationTime = new OperationTime("Date", new DateTime().toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        this.operationTime = new DateTime().toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.operatorClass = operatorClass;
        this.status = status;
        this.name = "";
        this.operatorNumber = "";
    }

    public static List<OrderOperatingRecord> parseList(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return Lists.newArrayList();
        }
        return JSON.parseArray(jsonString, OrderOperatingRecord.class);
    }

}
