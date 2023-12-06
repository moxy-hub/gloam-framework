package com.gloam.web.security;

import com.gloam.web.security.domain.LoginModel;
import com.gloamframework.web.response.WebResult;
import com.gloamframework.web.security.GloamSecurityCacheManager;
import com.gloamframework.web.security.annotation.Authentication;
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

    @GetMapping("/auth")
    @Authentication(hasAuth = "kk:oo")
    public WebResult<String> auth() {
        return WebResult.success("登录成功");
    }

    @PostMapping("/ae")
    @Authentication
    public WebResult<String> auth2() {
        return WebResult.success("登录成功2");
    }

    @PostMapping("/ae3")
    @Authentication(hasAuth = "sss:sss")
    public WebResult<String> auth3() {
        cacheManager.getCache().put("ddd", new LoginModel("user", "admin"));
        cacheManager.getCache().put("234", new LoginModel("user1", "admin1"));
        LoginModel loginModel = cacheManager.getCache().get("234", LoginModel.class);
        return WebResult.success("success");
    }
}
