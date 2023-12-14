package com.gloamframework.web.security;

import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import com.gloamframework.web.security.adapter.GloamWebSecurityConfigurerAdapter;
import com.gloamframework.web.security.envelope.WebEnvelopeConfigure;
import com.gloamframework.web.security.exception.GloamHttpSecurityAdapterApplyException;
import com.gloamframework.web.security.exception.GloamWebSecurityAdapterApplyException;
import com.gloamframework.web.security.filter.GloamFilterConfigure;
import com.gloamframework.web.security.match.GloamMachterConfigure;
import com.gloamframework.web.security.properties.SecurityProperties;
import com.gloamframework.web.security.rsa.RsaSecurityConfigure;
import com.gloamframework.web.security.token.TokenConfigure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * web安全配置
 * todo 4、加密实现 5、xss过滤器 6、重复提交过滤器
 *
 * @author 晓龙
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
@Import({
        GloamBasicAdapterConfigure.class,
        GloamFilterConfigure.class,
        GloamMachterConfigure.class,
        TokenConfigure.class,
        GloamSecurityCacheConfigure.class,
        RsaSecurityConfigure.class,
        WebEnvelopeConfigure.class
})
@Slf4j
public class GloamSecurityConfigure extends WebSecurityConfigurerAdapter {

    /**
     * HttpSecurity适配器
     */
    @Autowired
    private List<GloamHttpSecurityConfigurerAdapter> httpSecurityConfigurerAdapters;

    /**
     * WebSecurity适配器
     */
    @Autowired
    private List<GloamWebSecurityConfigurerAdapter> webSecurityConfigurerAdapters;

    @Override
    protected void configure(HttpSecurity http) {
        httpSecurityConfigurerAdapters.stream().sorted(Comparator.comparingInt(Ordered::getOrder)).forEachOrdered(adapter -> {
            try {
                http.apply(adapter);
                log.debug("gloam http security apply adapter success on order:{} # {}", adapter.getOrder(), adapter);
            } catch (Exception e) {
                throw new GloamHttpSecurityAdapterApplyException("gloam http security adapter apply fail", "配置适配器:" + adapter + "失败", e);
            }
        });
    }

    @Override
    public void init(WebSecurity web) throws Exception {
        webSecurityConfigurerAdapters.stream().sorted(Comparator.comparingInt(Ordered::getOrder)).forEachOrdered(adapter -> {
            try {
                web.apply(adapter);
                log.debug("gloam web security apply adapter success on order:{} # {}", adapter.getOrder(), adapter);
            } catch (Exception e) {
                throw new GloamWebSecurityAdapterApplyException("gloam web security adapter apply fail", "配置适配器:" + adapter + "失败", e);
            }
        });
        super.init(web);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
