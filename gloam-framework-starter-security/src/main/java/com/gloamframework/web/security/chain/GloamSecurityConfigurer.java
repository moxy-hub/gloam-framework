package com.gloamframework.web.security.chain;

import cn.hutool.core.collection.CollectionUtil;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import com.gloamframework.web.security.chain.annotation.SecurityChainAssemblerRegister;
import com.gloamframework.web.security.chain.assembler.SecurityChainAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * spring security 链配置
 *
 * @author 晓龙
 */
@Configurable
@Slf4j
public class GloamSecurityConfigurer extends WebSecurityConfigurerAdapter implements BeanFactoryAware {

    private static final SecurityChain securityChain = new SecurityChain();
    private BeanFactory beanFactory;
    private Set<SecurityChainAssembler> securityChainAssemblers;

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
        if (CollectionUtil.isEmpty(securityChainAssemblers)) {
            this.registerAssembler();
        }
        if (securityChain.getWebSecurity() == null || securityChain.getHttpSecurity() == null) {
            return;
        }
        for (SecurityChainAssembler chainAssembler : securityChainAssemblers) {
            chainAssembler.assemble(securityChain);
        }
    }

    private void registerAssembler() {
        if (CollectionUtil.isNotEmpty(securityChainAssemblers)) {
            return;
        }
        // 获取spring的扫描路径
        Set<String> packages = new HashSet<>();
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            log.warn("Could not obtain auto-configuration package, stop to discover web security chain assemblers");
        } else {
            packages.addAll(AutoConfigurationPackages.get(this.beanFactory));
        }
        log.info("web security chain assemblers search in package:{}", packages);
        ResourceScanner scanner = ResourceScanner.getDefault();
        Set<Class<?>> securityChainAssemblerClasses = new HashSet<>();
        packages.forEach(pkg -> {
            try {
                securityChainAssemblerClasses.addAll(scanner.scannerForClassesWithAnnotation(pkg, this.getClass().getClassLoader(), SecurityChainAssemblerRegister.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 实例
        for (Class<?> assemblerClass : securityChainAssemblerClasses) {
            try {
                Object instance = assemblerClass.newInstance();
                if (securityChainAssemblers == null) {
                    securityChainAssemblers = new HashSet<>();
                }
                securityChainAssemblers.add((SecurityChainAssembler) instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Autowired
    public void set(BeanFactory beanFactory){
        for (String s : AutoConfigurationPackages.get(beanFactory)) {
            System.out.println(s);
        }
    }
}
