package com.gloam.web.security;

import com.gloamframework.web.response.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 晓龙
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class TestApiController {

    @GetMapping("/auth")
    public WebResult<String> auth() {
        return WebResult.success("登录成功");
    }

}
