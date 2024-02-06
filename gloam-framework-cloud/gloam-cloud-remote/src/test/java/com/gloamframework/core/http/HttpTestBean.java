package com.gloamframework.core.http;

import com.gloamframework.cloud.remote.retrofit.annotation.WebService;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author 晓龙
 */
@WebService("http://127.0.0.1:6375/oa/account/login/")
public interface HttpTestBean {

    @GET("/")
    TestModel token(@Query("grant_type") String grantType, @Query("redirect_uri") String redirectUri, @Query("code") String code);

}
