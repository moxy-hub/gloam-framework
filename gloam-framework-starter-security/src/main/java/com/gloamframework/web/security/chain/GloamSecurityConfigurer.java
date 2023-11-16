package com.gloamframework.web.security.chain;

import com.gloamframework.web.security.chain.assembler.BasicSecurityChainConfigure;
import com.gloamframework.web.security.chain.assembler.SecurityChainAssembler;
import com.gloamframework.web.security.chain.exception.SecurityChainInitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring security 链配置
 *
 * @author 晓龙
 */
@Configurable
@Slf4j
@Import({BasicSecurityChainConfigure.class})
public class GloamSecurityConfigurer extends WebSecurityConfigurerAdapter implements BeanFactoryAware {

    private static final SecurityChain securityChain = new SecurityChain();
    private ListableBeanFactory beanFactory;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        securityChain.setHttpSecurity(http);
        this.transmit();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        securityChain.setWebSecurity(web);
        this.transmit();
    }

    private synchronized void transmit() throws Exception {
        if (securityChain.getWebSecurity() == null || securityChain.getHttpSecurity() == null) {
            return;
        }
        // 获取符合的bean
        for (SecurityChainAssembler securityChainAssembler : this.beanFactory.getBeansOfType(SecurityChainAssembler.class).values()) {
            securityChainAssembler.assemble(securityChain);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ListableBeanFactory) {
            this.beanFactory = (ListableBeanFactory) beanFactory;
        } else {
            throw new SecurityChainInitException("安全配置链获取ListableBeanFactory工厂失败", "Spring Security Chain configure fail");
        }
    }

}
