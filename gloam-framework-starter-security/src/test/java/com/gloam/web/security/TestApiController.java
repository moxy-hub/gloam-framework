package com.gloam.web.security;

import com.gloam.web.security.domain.LoginModel;
import com.gloamframework.cache.ExpireValue;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.annotation.Authentication;
import com.gloamframework.web.security.annotation.Token;
import com.gloamframework.web.security.token.TokenManager;
import com.gloamframework.web.security.token.constant.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 晓龙
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class TestApiController {

    @Autowired
    private GloamSecurityCacheManager cacheManager;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("/auth")
//    @Authentication
    @Token(strategy = Token.Strategy.WANT)
    public WebResult<String> auth() {
        throw new NullPointerException("error");
//        return WebResult.success("登录成功");
    }

    @PostMapping("/ae")
    @Authentication
    public WebResult<String> auth2() {
        return WebResult.success("登录成功2");
    }

    @PostMapping("/ae3")
    @Authentication(hasAuth = "sss:ss")
    @Token(strategy = Token.Strategy.NONE)
    public WebResult<String> auth3() {
        cacheManager.getCache().put("ttl-test", new ExpireValue(new LoginModel("user", "admin"), 30000));
        cacheManager.getCache().put("ttl@3333", new LoginModel("user1", "admin1"), 20000);
        LoginModel loginModel = cacheManager.getCache().get("ttl@3333", LoginModel.class);
        return WebResult.success("success");
    }

    @PostMapping("/login")
    @Token(strategy = Token.Strategy.NONE)
    public WebResult<String> login() {
        tokenManager.authenticate("123456", Device.PC);
        return WebResult.success("登录成功");
    }

}
