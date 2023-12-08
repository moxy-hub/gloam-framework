package com.gloam.web.security.filter;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


public class HttpAdapterTest extends GloamHttpSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.addFilter(new TestAuthFilter(authenticationManager));
    }
}
