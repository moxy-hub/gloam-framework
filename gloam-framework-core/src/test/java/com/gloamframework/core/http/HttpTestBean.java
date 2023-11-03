package com.gloamframework.core.http;

import com.gloamframework.core.http.annotation.WebService;
import retrofit2.http.GET;

/**
 * @author 晓龙
 */
@WebService("https://www.baidu.com")
public interface HttpTestBean {

    @GET("/")
    String list();

}
