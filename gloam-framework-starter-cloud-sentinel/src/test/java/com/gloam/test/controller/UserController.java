package com.gloam.test.controller;

import com.gloamframework.web.response.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ServiceTest serviceTest;

    @PostMapping("/login")
    public WebResult<Void> login(String username, String password) {
        return WebResult.successWithMessage(serviceTest.limit());
    }

}
