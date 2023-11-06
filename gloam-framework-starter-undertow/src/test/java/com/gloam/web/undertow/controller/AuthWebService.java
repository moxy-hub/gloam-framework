package com.gloam.web.undertow.controller;

import com.gloamframework.core.http.annotation.WebService;
import retrofit2.http.GET;

/**
 * @author 晓龙
 */
@WebService("http://127.0.0.1:6375/oa/account/login/")
public interface AuthWebService {

    @GET("/")
    Model auth();

}
