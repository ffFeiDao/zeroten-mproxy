package com.zeroten.mproxy.dao;

import com.google.common.collect.Maps;
import com.zeroten.mproxy.entity.OrderForm;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderFormDao extends BaseDao<OrderForm> {

    public OrderForm getByNumber(String number) {
        Map<String, Object> where = Maps.newHashMap();
        where.put("number", number);
        return findOne(OrderForm.class, where);
    }

    public void saveOrderState(OrderForm orderForm) {
        update(orderForm, "orderState");
    }
}
