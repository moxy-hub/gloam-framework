package com.gloamframework.web.security.match;

import cn.hutool.core.util.EnumUtil;
import com.gloamframework.common.lang.StringUtil;
import com.gloamframework.web.security.adapter.GloamHttpSecurityConfigurerAdapter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 扫描所有放行的注解:@PermitAll
 */
public abstract class AbstractSpringMvcPathMatcher<A extends Annotation> extends GloamHttpSecurityConfigurerAdapter implements BeanFactoryAware {

    @FunctionalInterface
    private interface AnnotationMatch {
        void match(String[] pathUrl, HttpMethod httpMethod);
    }

    private static final String DEFAULT_PATH_SEPARATOR = "/";
    private static final String POST_TYPE_NAME = "org.springframework.web.bind.annotation.PostMapping";
    private static final String GET_TYPE_NAME = "org.springframework.web.bind.annotation.GetMapping";
    private static final String PUT_TYPE_NAME = "org.springframework.web.bind.annotation.PutMapping";
    private static final String DELETE_TYPE_NAME = "org.springframework.web.bind.annotation.DeleteMapping";
    private static final String ANY_TYPE_NAME = "org.springframework.web.bind.annotation.RequestMapping";
    private static final String ALL_PATH_MATCH_SEPARATOR_SPRING = "/**";
    private static final String ALL_PATH_MATCH_SEPARATOR_SERVLET = "/*";

    private ServerProperties serverProperties;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!ListableBeanFactory.class.isAssignableFrom(beanFactory.getClass())) {
            return;
        }
        serverProperties = beanFactory.getBean(ServerProperties.class);
        String[] allControllerBeanNames = ((ListableBeanFactory) beanFactory).getBeanNamesForAnnotation(Controller.class);
        for (String restBeanName : allControllerBeanNames) {
            // 检查权限注解
            this.matchingAnnotation(ClassUtils.getUserClass(beanFactory.getBean(restBeanName).getClass()));
        }
    }

    /**
     * 自定义对应的注解类型
     */
    protected abstract Class<A> annotation();

    /**
     * 返回匹配到的注解
     */
    protected abstract void registerFilter(String pathUrl, HttpMethod httpMethod, A annotation);

    /**
     * 处理spring 上下文存在的问题
     */
    private void registerFilterWithContext(String pathUrl, HttpMethod httpMethod, A annotation) {
        String webContext = this.obtainSpringWebContext();
        if (StringUtil.isNotBlank(webContext)) {
            webContext = buildUrl(webContext, "", DEFAULT_PATH_SEPARATOR);
            pathUrl = webContext + pathUrl;
        }
        registerFilter(pathUrl, httpMethod, annotation);
    }

    private void matchingAnnotation(Class<?> beanClass) {
        // 获取路径
        analysisController(beanClass, (basePathUrl, httpMethod) -> {
            // 处理全部的基础路径
            for (String pathUrl : basePathUrl) {
                if (StringUtil.isNotBlank(pathUrl)) {
                    // 处理类上
                    this.matchClassAnnotation(pathUrl, beanClass, httpMethod);
                }
                // 处理方法上
                this.matchMethodAnnotation(pathUrl, beanClass);
            }
        });

    }

    private void matchClassAnnotation(String pathUrl, Class<?> beanClass, HttpMethod httpMethod) {
        A annotation = AnnotationUtils.findAnnotation(beanClass, this.annotation());
        if (annotation == null) {
            return;
        }
        String baseUrl = this.buildUrl(pathUrl, ALL_PATH_MATCH_SEPARATOR_SPRING, ALL_PATH_MATCH_SEPARATOR_SPRING, ALL_PATH_MATCH_SEPARATOR_SERVLET);
        // 拼接
        this.registerFilterWithContext(baseUrl, httpMethod, annotation);
    }

    private void analysisController(Class<?> beanClass, AnnotationMatch annotationMatch) {
        // 获取路径
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(beanClass, RequestMapping.class);
        if (requestMapping == null) {
            annotationMatch.match(new String[]{""}, null);
            return;
        }
        annotationMatch.match(requestMapping.path(), requestMapping.method().length > 0 ? EnumUtil.fromString(HttpMethod.class, requestMapping.method()[0].name()) : null);
    }

    private void matchMethodAnnotation(String pathUrl, Class<?> beanClass) {

        Method[] methodsWithAnnotation = MethodUtils.getMethodsWithAnnotation(ClassUtils.getUserClass(beanClass), this.annotation());
        String baseUrl = this.buildUrl(pathUrl, "", DEFAULT_PATH_SEPARATOR);
        for (Method method : methodsWithAnnotation) {
            // 效验获取
            A annotation = AnnotationUtils.findAnnotation(method, this.annotation());
            if (annotation == null) {
                continue;
            }
            Annotation[] allAnnotations = method.getAnnotations();
            for (Annotation eachAnnotation : allAnnotations) {
                this.matchMethodMVCAnnotation(eachAnnotation, (pathUrls, requestMethod) -> {
                    if (pathUrls.length < 1) {
                        return;
                    }
                    for (String url : pathUrls) {
                        if (StringUtils.isNotBlank(url) && !StringUtils.startsWith(url, DEFAULT_PATH_SEPARATOR)) {
                            url = DEFAULT_PATH_SEPARATOR + url;
                        }
                        this.registerFilterWithContext(String.format("%s%s", baseUrl, url), requestMethod, annotation);
                    }
                });
            }
        }
    }

    private void matchMethodMVCAnnotation(Annotation annotation, AnnotationMatch annotationMatch) {
        switch (annotation.annotationType().getTypeName()) {
            case POST_TYPE_NAME:
                PostMapping postMapping = (PostMapping) annotation;
                String[] postPathUrls = postMapping.path().length > 0 ? postMapping.path() : postMapping.value();
                annotationMatch.match(postPathUrls.length > 0 ? postPathUrls : new String[]{""}, HttpMethod.POST);
                break;
            case GET_TYPE_NAME:
                GetMapping getMapping = (GetMapping) annotation;
                String[] getPathUrls = getMapping.path().length > 0 ? getMapping.path() : getMapping.value();
                annotationMatch.match(getPathUrls.length > 0 ? getPathUrls : new String[]{""}, HttpMethod.GET);
                break;
            case PUT_TYPE_NAME:
                PutMapping put = (PutMapping) annotation;
                String[] putPath = put.path().length > 0 ? put.path() : put.value();
                annotationMatch.match(putPath.length > 0 ? putPath : new String[]{""}, HttpMethod.PUT);
                break;
            case DELETE_TYPE_NAME:
                DeleteMapping delete = (DeleteMapping) annotation;
                String[] deletePath = delete.path().length > 0 ? delete.path() : delete.value();
                annotationMatch.match(deletePath.length > 0 ? deletePath : new String[]{""}, HttpMethod.DELETE);
                break;
            case ANY_TYPE_NAME:
                RequestMapping request = (RequestMapping) annotation;
                String[] requestPath = request.path().length > 0 ? request.path() : request.value();
                annotationMatch.match(requestPath.length > 0 ? requestPath : new String[]{""}, request.method().length > 0 ? EnumUtils.getEnum(HttpMethod.class, request.method()[0].name()) : null);
                break;
            default:
                annotationMatch.match(new String[0], null);
                break;
        }
    }

    private String buildUrl(String url, String suffix, String... notEndWith) {
        if (!StringUtils.startsWith(url, DEFAULT_PATH_SEPARATOR)) {
            url = DEFAULT_PATH_SEPARATOR + url;
        }
        if (!StringUtils.endsWithAny(url, notEndWith)) {
            url = url + suffix;
        }
        return url;
    }

    /**
     * 获取spring的上下文
     */
    private String obtainSpringWebContext() {
        ServerProperties.Servlet servlet = serverProperties.getServlet();
        if (Objects.isNull(servlet)) {
            return "";
        }
        return StringUtil.isBlank(servlet.getContextPath()) ? "" : servlet.getContextPath();
    }

}
