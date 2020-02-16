package com.zeroten.mproxy.service;

import com.zeroten.mproxy.model.response.BaseResponse;

public interface ResetService {

    BaseResponse reset(String password);

}
