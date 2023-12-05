package com.gloam.web.security.filter;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2023年12月05日 12:53
 */
@Component
public class HttpAdapterTest extends GloamHttpSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.addFilter(new TestAuthFilter(authenticationManager));
    }
}
