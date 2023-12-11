package com.gloam.web.security;

import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.GloamSecurityContext;
import com.gloamframework.web.security.annotation.Token;
import com.gloamframework.web.security.token.constant.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
@Token(strategy = Token.Strategy.NONE)
public class UserController {

    @PostMapping("/login")
    public WebResult<Void> login(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            GloamSecurityContext.passAuthenticationWithResponseHeader(username, password, Device.PC);
            return WebResult.success(null, "登录成功");
        }
        return WebResult.fail("用户名密码错误");
    }

    @GetMapping
    @Token(strategy = Token.Strategy.NEED)
    public WebResult<String> pullUserInfo() {
        return WebResult.success("用户信息");
    }

}
