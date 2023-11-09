package com.gloam.web.security.assembler;

import com.gloam.web.security.filter.TestSuccennFilter;
import com.gloamframework.web.security.chain.SecurityChain;
import com.gloamframework.web.security.chain.annotation.SecurityChainAssemblerRegister;
import com.gloamframework.web.security.chain.assembler.SecurityChainAssembler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

/**
 * @author 晓龙
 */
@SecurityChainAssemblerRegister
public class TestSecurityChain implements SecurityChainAssembler {

    @Override
    public void assemble(SecurityChain securityChain) throws Exception {
        HttpSecurity httpSecurity = securityChain.getHttpSecurity();
        httpSecurity
                // 允许跨域
				.cors().and()
                // 关闭预防攻击
                .addFilterAfter(new TestSuccennFilter(), CsrfFilter.class)
                .formLogin().successForwardUrl("/api/auth")
                // 拦截全部请求
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                // 异常处理器
                .and()
                .servletApi();;
    }

}
