package com.zeroten.mproxy.controller;

import com.zeroten.mproxy.constant.UrlMapping;
import com.zeroten.mproxy.model.response.BaseResponse;
import com.zeroten.mproxy.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ResetController {

    @Autowired
    private ResetService resetService;

    @GetMapping(UrlMapping.RESET)
    public BaseResponse reset(@RequestParam(value = "pwd", required = false) String password) {
        return resetService.reset(password);
    }

}
