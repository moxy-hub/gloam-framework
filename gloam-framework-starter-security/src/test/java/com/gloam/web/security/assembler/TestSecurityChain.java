package com.gloam.web.security.assembler;

import com.gloam.web.security.filter.TestSuccennFilter;
import com.gloamframework.web.security.chain.SecurityChain;
import com.gloamframework.web.security.chain.assembler.SecurityChainAssembler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;

@Component
public class TestSecurityChain implements SecurityChainAssembler {

    @Override
    public void assemble(SecurityChain securityChain) throws Exception {
        HttpSecurity httpSecurity = securityChain.getHttpSecurity();
        httpSecurity
                .addFilterAfter(new TestSuccennFilter(), CsrfFilter.class)
                // 拦截全部请求
                .authorizeRequests()
                .anyRequest().authenticated()
                // 异常处理器
                .and()
                .servletApi();
//        ;
    }

}
