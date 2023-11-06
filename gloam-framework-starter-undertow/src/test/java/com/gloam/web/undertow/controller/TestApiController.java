package com.gloam.web.undertow.controller;

import com.gloamframework.core.http.annotation.WebServiceInject;
import com.gloamframework.web.response.WebResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 晓龙
 */
@RestController
@RequestMapping("/api")
public class TestApiController {

    @WebServiceInject
    private AuthWebService authWebService;

    @GetMapping("/auth")
    public WebResult<Model> auth(){
        return WebResult.success(authWebService.auth());
    }

}
