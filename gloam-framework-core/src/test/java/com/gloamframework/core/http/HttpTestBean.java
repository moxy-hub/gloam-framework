package com.gloamframework.core.http;

import com.gloamframework.core.http.annotation.RemoteClient;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author 晓龙
 */
@RemoteClient
public interface HttpTestBean {

    @GET("users/{user}/repos")
    Call<Object> list(@Path("user") String user);

}
