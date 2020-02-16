package com.zeroten.mproxy.controller;

import com.zeroten.mproxy.constant.UrlMapping;
import com.zeroten.mproxy.entity.Account;
import com.zeroten.mproxy.model.request.AccountRequest;
import com.zeroten.mproxy.model.request.OrderQueryRequest;
import com.zeroten.mproxy.model.request.PushBindRequest;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping
public class PushController {

    @Autowired
    private PushService pushService;

    @GetMapping(UrlMapping.ACCOUNT_INFO)
    public BaseResponse<List<Account>> getAccount(@RequestParam(value = "telephone", required = false) String telephone,
                                                  @RequestParam(value = "key", required = false) String key,
                                                  @RequestParam(value = "timestamp", required = false) String timestamp,
                                                  @RequestParam(value = "sign", required = false) String sign) {
        return pushService.getAccount(telephone, key, timestamp, sign);
    }


    @GetMapping(UrlMapping.ACCOUNT_INFO_UPDATE)
    public BaseResponse accountInfoUpdate(@RequestParam("clientId") String clientId,
                                          @RequestParam("clerkState") Integer clerkState,
                                          @RequestParam("key") String key,
                                          @RequestParam("timestamp") String timestamp,
                                          @RequestParam("sign") String sign,
                                          HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization,content-type");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        return pushService.updateAccount(new AccountRequest(clientId, clerkState, key, timestamp, sign));
    }

    @GetMapping(UrlMapping.USER_PUSH_BIND)
    public BaseResponse pushBindGet(@RequestParam("clientId") String clientId,
                                    @RequestParam("clientType") String clientType,
                                    @RequestParam("registrationId") String registrationId,
                                    @RequestParam("key") String key,
                                    @RequestParam("timestamp") String timestamp,
                                    @RequestParam("sign") String sign) {
        return pushService.bind(new PushBindRequest(clientId, clientType, registrationId, key, timestamp, sign));
    }

    @PostMapping(UrlMapping.USER_PUSH_BIND)
    public BaseResponse pushBindPost(@RequestBody PushBindRequest request) {
        return pushService.bind(request);
    }

    @PostMapping(UrlMapping.USER_PUSH_UNBIND)
    public BaseResponse pushUnbind(@RequestBody PushBindRequest request) {
        return pushService.unbind(request);
    }

    @GetMapping(UrlMapping.ORDER_QUERY)
    public BaseResponse orderQuery(@RequestParam("appId") String appId,
                                   @RequestParam("orderNo") String orderNo,
                                   @RequestParam("key") String key,
                                   @RequestParam("timestamp") String timestamp,
                                   @RequestParam("sign") String sign) {
        return pushService.orderQuery(new OrderQueryRequest(appId, null, orderNo, key, timestamp, sign));
    }
}
