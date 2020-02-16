package com.zeroten.mproxy.controller;

import cn.leancloud.AVUser;
import com.zeroten.mproxy.constant.UrlMapping;
import com.zeroten.mproxy.model.request.LoginRequest;
import com.zeroten.mproxy.model.response.LoginResponse;
import com.zeroten.mproxy.thirdparty.leancloud.LcTool;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class UserController {

    @ResponseBody
    @PostMapping(UrlMapping.USER_LOGIN)
    public LoginResponse login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = new LoginResponse();

        try {
            AVUser.logIn(request.getUsername(), request.getPassword());
            AVUser loginUser = AVUser.currentUser();
            if (loginUser == null) {
                loginResponse.setCode("login fail");
            } else {
                loginResponse.setCode(loginUser.getSessionToken());
            }
        } catch (Exception e) {
            loginResponse.setCode("login error");
        }

        return loginResponse;
    }

    @ResponseBody
    @GetMapping(UrlMapping.USER_INFO)
    public LoginResponse info(HttpServletRequest request) {
        LoginResponse loginResponse = new LoginResponse();

        AVUser loginUser = LcTool.getLoginUser(request);
        if (loginUser == null) {
            loginResponse.setCode("no login");
        } else {
            loginResponse.setCode(loginUser.getUsername() + "," + loginUser.getSessionToken());
        }
        return loginResponse;
    }

    @ResponseBody
    @GetMapping(UrlMapping.USER_LOGOUT)
    public void logout() {
        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            user.logOut();
        }
    }
}
