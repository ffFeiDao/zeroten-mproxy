package com.zeroten.mproxy.service;

import com.zeroten.mproxy.entity.Account;
import com.zeroten.mproxy.model.request.AccountRequest;
import com.zeroten.mproxy.model.request.OrderQueryRequest;
import com.zeroten.mproxy.model.request.PushBindRequest;
import com.zeroten.mproxy.model.response.BaseResponse;

import java.util.List;

public interface PushService {

    BaseResponse<List<Account>> getAccount(String mobile, String key, String timestamp, String sign);

    BaseResponse updateAccount(AccountRequest request);

    BaseResponse bind(PushBindRequest request);

    BaseResponse unbind(PushBindRequest request);

    BaseResponse orderQuery(OrderQueryRequest request);

}
