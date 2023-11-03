package com.gloamframework.core.http.bean;

import com.gloamframework.core.boot.scanner.ResourceScanner;
import com.gloamframework.core.http.annotation.RemoteClient;
import com.gloamframework.core.http.exception.HttpInterfaceBeanRegisterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 注册httpRetrofitBean
 *
 * @author 晓龙
 */
@Slf4j
public class HttpRetrofitBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private static final String DEFAULT_SCANNER_PACKAGE = "com.gloamframework";

    /**
     * gloam scanner
     */
    private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();

    /**
     * spring bean factory
     */
    private BeanFactory beanFactory;

    /**
     * spring bean factory aware
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry, BeanNameGenerator beanNameGenerator) {
        // 获取spring的扫描路径
        Set<String> packages = new HashSet<>();
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            packages.add(DEFAULT_SCANNER_PACKAGE);
            log.warn("Could not obtain auto-configuration package, fallback use default:{}", DEFAULT_SCANNER_PACKAGE);
        } else {
            packages.addAll(AutoConfigurationPackages.get(this.beanFactory));
        }
        log.info("http retrofit2 start in package:{}", packages);
        Set<Class<?>> allClasses = new HashSet<>();
        packages.forEach(eachPackage -> {
            try {
                allClasses.addAll(resourceScanner.scannerForClassesWithAnnotation(eachPackage, this.getClass().getClassLoader(), RemoteClient.class));
            } catch (IOException e) {
                throw new HttpInterfaceBeanRegisterException("IO异常", "加载RemoteClient资源失败", e);
            }
        });
        // 对符合的class创建beanDefinition
        allClasses.forEach(beanClass -> {
            if (!beanClass.isInterface() || beanClass.isAnnotation()) {
                throw new HttpInterfaceBeanRegisterException("标注@RemoteClient必须是接口！", "创建retrofit2BeanDefinition失败");
            }
            // 创建BeanDefinition
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            // 设置工厂bean的构造器参数
            beanDefinition.getConstructorArgumentValues()
                    .addGenericArgumentValue(beanClass);
            // 将beanClass设置为工厂bean的代理对象
            beanDefinition.setBeanClass(HttpRetrofitFactoryBean.class);
            // 生成beanName
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            // 注册
            registry.registerBeanDefinition(beanName, beanDefinition);
            log.trace("create http retrofit bean:{} from:{}", beanName, beanClass);
        });
    }


}
