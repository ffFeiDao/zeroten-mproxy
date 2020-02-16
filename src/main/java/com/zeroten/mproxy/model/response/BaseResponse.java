package com.zeroten.mproxy.model.response;

import com.zeroten.mproxy.constant.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(T data) {
        this.code = 0;
        this.message = Constants.SUCCESS;
        this.data = data;
    }
}
