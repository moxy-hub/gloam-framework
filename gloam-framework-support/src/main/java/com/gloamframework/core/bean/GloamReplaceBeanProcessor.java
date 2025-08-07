package com.gloamframework.core.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.MethodMetadata;

import java.util.Objects;

/**
 * @author 晓龙
 * @version 1.8.271
 * @protectName gloam-framework
 * @date 2025年08月06日 15:24
 */
public class GloamReplaceBeanProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final String ATTRIBUTE_NAME_TARGETS = "targets";
    private static final String ATTRIBUTE_NAME_FORCE = "force";
    private static final String PATTERN_4_NEW_BEAN_NAME = "{}#gloam#{}";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        for (String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
            // 不处理不是以@Bean注入的bean
            if (!AnnotatedBeanDefinition.class.isAssignableFrom(beanDefinition.getClass())) {
                continue;
            }
            MethodMetadata methodMetadata = ((AnnotatedBeanDefinition) beanDefinition).getFactoryMethodMetadata();
            if (Objects.isNull(methodMetadata) || !methodMetadata.isAnnotated(ReplaceBean.class.getName())) {
                continue;
            }
            // 获取ReplaceBean
            MergedAnnotation<ReplaceBean> replaceBeanMergedAnnotation = methodMetadata.getAnnotations().get(ReplaceBean.class);
            // 处理多余的Bean
            String[] targets = replaceBeanMergedAnnotation.getStringArray(ATTRIBUTE_NAME_TARGETS);
            boolean force = replaceBeanMergedAnnotation.getBoolean(ATTRIBUTE_NAME_FORCE);
            // 全部处理完或者删除完
            this.handlerReplaceBeans(targets, force, beanDefinitionRegistry);
            // 处理当前bean
            if (!force) {
                beanDefinition.setPrimary(true);
            } else {
                for (String target : targets) {
                    beanDefinitionRegistry.registerBeanDefinition(target, beanDefinition);
                }
            }
            beanDefinitionRegistry.removeBeanDefinition(beanDefinitionName);
            beanDefinitionRegistry.registerBeanDefinition(beanDefinitionName, beanDefinition);

        }
    }

    private void handlerReplaceBeans(String[] targets, boolean force, BeanDefinitionRegistry beanDefinitionRegistry) {
        for (String target : targets) {
            if (!beanDefinitionRegistry.containsBeanDefinition(target)) {
                continue;
            }
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(target);

            // todo 检查类型，如果和当前bean不一致，则不能进行处理
            beanDefinitionRegistry.removeBeanDefinition(target);
            if (force) {
                continue;
            }
            beanDefinition.setPrimary(false);
            beanDefinitionRegistry.registerBeanDefinition(target, beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

}
