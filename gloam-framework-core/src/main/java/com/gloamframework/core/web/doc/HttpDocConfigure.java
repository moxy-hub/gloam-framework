package com.gloamframework.core.web.doc;

import cn.hutool.core.collection.CollectionUtil;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@EnableSwagger2
@EnableSwaggerBootstrapUI
@Configurable
@Slf4j
public class HttpDocConfigure {

    /**
     * gloam scanner
     */
    private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();

    private static final String HTTP_DOCKET_BEAN_NAME = "http-doc-%s";

    @PostConstruct
    private void log() {
        log.info("Http doc started at url /doc.html");
    }

    private void registerHttpDocBean(ApplicationContext applicationContext, ConstructorArgumentValues cav) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        RootBeanDefinition beanDf = new RootBeanDefinition(HttpDocDocket.class);
        beanDf.setConstructorArgumentValues(cav);
        beanFactory.registerBeanDefinition(String.format(HTTP_DOCKET_BEAN_NAME, UUID.randomUUID()), beanDf);
    }

    @Autowired
    private void startupHttpDoc(ApplicationContext applicationContext) {
        // 进行扫描，获取启动注解
        Map<String, Object> enableAnnotationBean = applicationContext.getBeansWithAnnotation(EnableHttpDoc.class);
        if (CollectionUtil.isEmpty(enableAnnotationBean)) {
            throw new HttpDocInitializeException("初始化在线文档失败，没有正确识别到启动注解");
        }
        // 如果有多个启动注解，只进行识别第一个注解路径
        String basePackage = enableAnnotationBean.values().stream()
                .findFirst().orElseThrow(() -> new HttpDocInitializeException("初始化在线文档失败，获取基础扫描路径失败"))
                .getClass().getPackage().getName();
        // 进行扫描
        List<Class<?>> packageInfoList = null;
        try {
            packageInfoList = resourceScanner.scannerForClasses(basePackage, this.getClass().getClassLoader(), "package-info");
        } catch (IOException e) {
            throw new HttpDocInitializeException("获取swagger包信息资源失败", "swagger配置错误", e);
        }
        if (CollectionUtil.isEmpty(packageInfoList)) {
            // 没有package-info，直接创建docket
            this.registerHttpDocBean(applicationContext, this.assembleDefaultConstructorArgValue(basePackage));
            return;
        }
        // 根据扫描进行创建分组文档
        for (Class<?> aClass : packageInfoList) {
            DocGroup annoInfo = aClass.getPackage().getAnnotation(DocGroup.class);
            if (annoInfo == null) {
                // 没有注解，默认不进行创建该包下的文档
                continue;
            }
            this.registerHttpDocBean(applicationContext,
                    this.assembleConstructorArgValue(
                            annoInfo.title(),
                            annoInfo.tags(),
                            StringUtils.isEmpty(annoInfo.basePackage()) ? aClass.getPackage().getName() : annoInfo.basePackage(),
                            annoInfo.version(),
                            annoInfo.leader(),
                            annoInfo.description(),
                            annoInfo.serviceUrl()
                    ));
        }
    }

    private ConstructorArgumentValues assembleDefaultConstructorArgValue(String basePackage) {
        return assembleConstructorArgValue(
                "httpDoc",
                "gloam",
                basePackage,
                "1.0.0",
                "gloam",
                "gloam framework",
                "null"
        );
    }

    private ConstructorArgumentValues assembleConstructorArgValue(String title, String apiName, String basePackage, String version,
                                                                  String leader, String description, String serviceUrl) {
        ConstructorArgumentValues cav = new ConstructorArgumentValues();
        cav.addIndexedArgumentValue(0, title);
        cav.addIndexedArgumentValue(1, apiName);
        cav.addIndexedArgumentValue(2, basePackage);
        cav.addIndexedArgumentValue(3, version);
        cav.addIndexedArgumentValue(4, leader);
        cav.addIndexedArgumentValue(5, description);
        cav.addIndexedArgumentValue(6, serviceUrl);
        return cav;
    }
}
