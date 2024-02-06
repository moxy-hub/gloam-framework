package com.gloam.web.security;

import com.gloamframework.web.response.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
@Slf4j
public class UserController {

    @PostMapping("/xss")
    public WebResult<String> testXss(String message) {
        return WebResult.success(message);
    }
}
