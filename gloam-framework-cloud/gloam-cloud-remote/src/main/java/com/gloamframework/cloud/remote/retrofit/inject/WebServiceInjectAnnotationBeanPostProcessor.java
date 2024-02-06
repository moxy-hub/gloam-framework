package com.gloamframework.cloud.remote.retrofit.inject;

import com.gloamframework.cloud.remote.retrofit.annotation.WebService;
import com.gloamframework.cloud.remote.retrofit.annotation.WebServiceInject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.support.LookupOverride;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现@WebServiceInject注入功能
 *
 * @author 晓龙
 */
@Slf4j
public class WebServiceInjectAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements MergedBeanDefinitionPostProcessor, PriorityOrdered, BeanFactoryAware {

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(1);

    private String requiredParameterName = "required";

    private boolean requiredParameterValue = true;

    private int order = Ordered.LOWEST_PRECEDENCE - 3;

    @Nullable
    private ConfigurableListableBeanFactory beanFactory;

    private final Set<String> lookupMethodsChecked = Collections.newSetFromMap(new ConcurrentHashMap<>(256));

    private final Map<Class<?>, Constructor<?>[]> candidateConstructorsCache = new ConcurrentHashMap<>(256);

    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);


    /**
     * 创建spring对于@WebServiceInject注解的注入支持
     */
    @SuppressWarnings("unchecked")
    public WebServiceInjectAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(WebServiceInject.class);
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "WebServiceInjectAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, beanType, null);
        metadata.checkConfigMembers(beanDefinition);
    }

    @Override
    public void resetBeanDefinition(String beanName) {
        this.lookupMethodsChecked.remove(beanName);
        this.injectionMetadataCache.remove(beanName);
    }

    @Override
    @Nullable
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, final String beanName)
            throws BeanCreationException {

        // Let's check for lookup methods here...
        if (!this.lookupMethodsChecked.contains(beanName)) {
            if (AnnotationUtils.isCandidateClass(beanClass, Lookup.class)) {
                try {
                    Class<?> targetClass = beanClass;
                    do {
                        ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                            Lookup lookup = method.getAnnotation(Lookup.class);
                            if (lookup != null) {
                                Assert.state(this.beanFactory != null, "No BeanFactory available");
                                LookupOverride override = new LookupOverride(method, lookup.value());
                                try {
                                    RootBeanDefinition mbd = (RootBeanDefinition)
                                            this.beanFactory.getMergedBeanDefinition(beanName);
                                    mbd.getMethodOverrides().addOverride(override);
                                } catch (NoSuchBeanDefinitionException ex) {
                                    throw new BeanCreationException(beanName,
                                            "Cannot apply @Lookup to beans without corresponding bean definition");
                                }
                            }
                        });
                        targetClass = targetClass.getSuperclass();
                    }
                    while (targetClass != null && targetClass != Object.class);

                } catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName, "Lookup method resolution failed", ex);
                }
            }
            this.lookupMethodsChecked.add(beanName);
        }

        // Quick check on the concurrent map first, with minimal locking.
        Constructor<?>[] candidateConstructors = this.candidateConstructorsCache.get(beanClass);
        if (candidateConstructors == null) {
            // Fully synchronized resolution now...
            synchronized (this.candidateConstructorsCache) {
                candidateConstructors = this.candidateConstructorsCache.get(beanClass);
                if (candidateConstructors == null) {
                    Constructor<?>[] rawCandidates;
                    try {
                        rawCandidates = beanClass.getDeclaredConstructors();
                    } catch (Throwable ex) {
                        throw new BeanCreationException(beanName,
                                "Resolution of declared constructors on bean Class [" + beanClass.getName() +
                                        "] from ClassLoader [" + beanClass.getClassLoader() + "] failed", ex);
                    }
                    List<Constructor<?>> candidates = new ArrayList<>(rawCandidates.length);
                    Constructor<?> requiredConstructor = null;
                    Constructor<?> defaultConstructor = null;
                    Constructor<?> primaryConstructor = BeanUtils.findPrimaryConstructor(beanClass);
                    int nonSyntheticConstructors = 0;
                    for (Constructor<?> candidate : rawCandidates) {
                        if (!candidate.isSynthetic()) {
                            nonSyntheticConstructors++;
                        } else if (primaryConstructor != null) {
                            continue;
                        }
                        MergedAnnotation<?> ann = findAutowiredAnnotation(candidate);
                        if (ann == null) {
                            Class<?> userClass = ClassUtils.getUserClass(beanClass);
                            if (userClass != beanClass) {
                                try {
                                    Constructor<?> superCtor =
                                            userClass.getDeclaredConstructor(candidate.getParameterTypes());
                                    ann = findAutowiredAnnotation(superCtor);
                                } catch (NoSuchMethodException ex) {
                                    // Simply proceed, no equivalent superclass constructor found...
                                }
                            }
                        }
                        if (ann != null) {
                            if (requiredConstructor != null) {
                                throw new BeanCreationException(beanName,
                                        "Invalid WebServiceInject-marked constructor: " + candidate +
                                                ". Found constructor with 'required' WebServiceInject annotation already: " +
                                                requiredConstructor);
                            }
                            boolean required = determineRequiredStatus(ann);
                            if (required) {
                                if (!candidates.isEmpty()) {
                                    throw new BeanCreationException(beanName,
                                            "Invalid WebServiceInject-marked constructors: " + candidates +
                                                    ". Found constructor with 'required' WebServiceInject annotation: " +
                                                    candidate);
                                }
                                requiredConstructor = candidate;
                            }
                            candidates.add(candidate);
                        } else if (candidate.getParameterCount() == 0) {
                            defaultConstructor = candidate;
                        }
                    }
                    if (!candidates.isEmpty()) {
                        // Add default constructor to list of optional constructors, as fallback.
                        if (requiredConstructor == null) {
                            if (defaultConstructor != null) {
                                candidates.add(defaultConstructor);
                            } else if (candidates.size() == 1 && log.isInfoEnabled()) {
                                log.info("Inconsistent constructor declaration on bean with name '" + beanName +
                                        "': single WebServiceInject-marked constructor flagged as optional - " +
                                        "this constructor is effectively required since there is no " +
                                        "default constructor to fall back to: " + candidates.get(0));
                            }
                        }
                        candidateConstructors = candidates.toArray(new Constructor<?>[0]);
                    } else if (rawCandidates.length == 1 && rawCandidates[0].getParameterCount() > 0) {
                        candidateConstructors = new Constructor<?>[]{rawCandidates[0]};
                    } else if (nonSyntheticConstructors == 2 && primaryConstructor != null &&
                            defaultConstructor != null && !primaryConstructor.equals(defaultConstructor)) {
                        candidateConstructors = new Constructor<?>[]{primaryConstructor, defaultConstructor};
                    } else if (nonSyntheticConstructors == 1 && primaryConstructor != null) {
                        candidateConstructors = new Constructor<?>[]{primaryConstructor};
                    } else {
                        candidateConstructors = new Constructor<?>[0];
                    }
                    this.candidateConstructorsCache.put(beanClass, candidateConstructors);
                }
            }
        }
        return (candidateConstructors.length > 0 ? candidateConstructors : null);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of WebServiceInject dependencies failed", ex);
        }
        return pvs;
    }

    @Deprecated
    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {

        return postProcessProperties(pvs, bean, beanName);
    }

    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz, @Nullable PropertyValues pvs) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    metadata = buildAutowiringMetadata(clazz);
                    this.injectionMetadataCache.put(cacheKey, metadata);
                }
            }
        }
        return metadata;
    }

    private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
        if (!AnnotationUtils.isCandidateClass(clazz, this.autowiredAnnotationTypes)) {
            return InjectionMetadata.EMPTY;
        }

        List<InjectionMetadata.InjectedElement> elements = new ArrayList<>();
        Class<?> targetClass = clazz;

        do {
            final List<InjectionMetadata.InjectedElement> currElements = new ArrayList<>();

            ReflectionUtils.doWithLocalFields(targetClass, field -> {
                MergedAnnotation<?> ann = findAutowiredAnnotation(field);
                if (ann != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (log.isInfoEnabled()) {
                            log.info("WebServiceInject annotation is not supported on static fields: " + field);
                        }
                        return;
                    }
                    // 只支持和内部WebService同时使用
                    Class<?> fieldType = field.getType();
                    if (AnnotationUtils.findAnnotation(fieldType, WebService.class) == null || !fieldType.isInterface() || fieldType.isAnnotation()) {
                        log.warn("WebServiceInject annotation is not supported on interface:{} without @WebService", fieldType);
                        return;
                    }
                    boolean required = determineRequiredStatus(ann);
                    currElements.add(new AutowiredFieldElement(field, required));
                }
            });

            ReflectionUtils.doWithLocalMethods(targetClass, method -> {
                Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                    return;
                }
                MergedAnnotation<?> ann = findAutowiredAnnotation(bridgedMethod);
                if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        if (log.isInfoEnabled()) {
                            log.info("Autowired annotation is not supported on static methods: " + method);
                        }
                        return;
                    }
                    if (method.getParameterCount() == 0) {
                        if (log.isInfoEnabled()) {
                            log.info("Autowired annotation should only be used on methods with parameters: " +
                                    method);
                        }
                    }
                    boolean required = determineRequiredStatus(ann);
                    PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                    currElements.add(new WenServiceInjectMethodElement(method, required, pd));
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return InjectionMetadata.forElements(elements, clazz);
    }

    @Nullable
    private MergedAnnotation<?> findAutowiredAnnotation(AccessibleObject ao) {
        MergedAnnotations annotations = MergedAnnotations.from(ao);
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
            MergedAnnotation<?> annotation = annotations.get(type);
            if (annotation.isPresent()) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * Determine if the annotated field or method requires its dependency.
     * <p>A 'required' dependency means that autowiring should fail when no beans
     * are found. Otherwise, the autowiring process will simply bypass the field
     * or method when no beans are found.
     *
     * @param ann the Autowired annotation
     * @return whether the annotation indicates that a dependency is required
     */
    @SuppressWarnings({"deprecation", "cast"})
    protected boolean determineRequiredStatus(MergedAnnotation<?> ann) {
        // The following (AnnotationAttributes) cast is required on JDK 9+.
        return determineRequiredStatus((AnnotationAttributes)
                ann.asMap(mergedAnnotation -> new AnnotationAttributes(mergedAnnotation.getType())));
    }

    /**
     * Determine if the annotated field or method requires its dependency.
     * <p>A 'required' dependency means that autowiring should fail when no beans
     * are found. Otherwise, the autowiring process will simply bypass the field
     * or method when no beans are found.
     *
     * @param ann the Autowired annotation
     * @return whether the annotation indicates that a dependency is required
     * @deprecated since 5.2, in favor of {@link #determineRequiredStatus(MergedAnnotation)}
     */
    @Deprecated
    private boolean determineRequiredStatus(AnnotationAttributes ann) {
        return (!ann.containsKey(this.requiredParameterName) ||
                this.requiredParameterValue == ann.getBoolean(this.requiredParameterName));
    }

    /**
     * Register the specified bean as dependent on the autowired beans.
     */
    private void registerDependentBeans(@Nullable String beanName, Set<String> autowiredBeanNames) {
        if (beanName != null) {
            for (String autowiredBeanName : autowiredBeanNames) {
                if (this.beanFactory != null && this.beanFactory.containsBean(autowiredBeanName)) {
                    this.beanFactory.registerDependentBean(autowiredBeanName, beanName);
                }
                if (log.isTraceEnabled()) {
                    log.trace("WebService Injecting by type from bean name '" + beanName +
                            "' to bean named '" + autowiredBeanName + "'");
                }
            }
        }
    }

    /**
     * Resolve the specified cached method argument or field value.
     */
    @Nullable
    private Object resolvedCachedArgument(@Nullable String beanName, @Nullable Object cachedArgument) {
        if (cachedArgument instanceof DependencyDescriptor) {
            DependencyDescriptor descriptor = (DependencyDescriptor) cachedArgument;
            Assert.state(this.beanFactory != null, "No BeanFactory available");
            return this.beanFactory.resolveDependency(descriptor, beanName, null, null);
        } else {
            return cachedArgument;
        }
    }


    /**
     * Class representing injection information about an annotated field.
     */
    private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        @Nullable
        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, null);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            Object value;
            if (this.cached) {
                value = resolvedCachedArgument(beanName, this.cachedFieldValue);
            } else {
                DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                Assert.state(beanFactory != null, "No BeanFactory available");
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                try {
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                } catch (BeansException ex) {
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
                }
                synchronized (this) {
                    if (!this.cached) {
                        if (value != null || this.required) {
                            this.cachedFieldValue = desc;
                            registerDependentBeans(beanName, autowiredBeanNames);
                            if (autowiredBeanNames.size() == 1) {
                                String autowiredBeanName = autowiredBeanNames.iterator().next();
                                if (beanFactory.containsBean(autowiredBeanName) &&
                                        beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                    this.cachedFieldValue = new ShortcutDependencyDescriptor(
                                            desc, autowiredBeanName, field.getType());
                                }
                            }
                        } else {
                            this.cachedFieldValue = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }
        }
    }


    /**
     * Class representing injection information about an annotated method.
     */
    private class WenServiceInjectMethodElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        @Nullable
        private volatile Object[] cachedMethodArguments;

        public WenServiceInjectMethodElement(Method method, boolean required, @Nullable PropertyDescriptor pd) {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, @Nullable String beanName, @Nullable PropertyValues pvs) throws Throwable {
            if (checkPropertySkipping(pvs)) {
                return;
            }
            Method method = (Method) this.member;
            Object[] arguments;
            if (this.cached) {
                // Shortcut for avoiding synchronization...
                arguments = resolveCachedArguments(beanName);
            } else {
                int argumentCount = method.getParameterCount();
                arguments = new Object[argumentCount];
                DependencyDescriptor[] descriptors = new DependencyDescriptor[argumentCount];
                Set<String> autowiredBeans = new LinkedHashSet<>(argumentCount);
                Assert.state(beanFactory != null, "No BeanFactory available");
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                for (int i = 0; i < arguments.length; i++) {
                    MethodParameter methodParam = new MethodParameter(method, i);
                    DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
                    currDesc.setContainingClass(bean.getClass());
                    descriptors[i] = currDesc;
                    try {
                        Object arg = beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    } catch (BeansException ex) {
                        throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
                    }
                }
                synchronized (this) {
                    if (!this.cached) {
                        if (arguments != null) {
                            DependencyDescriptor[] cachedMethodArguments = Arrays.copyOf(descriptors, arguments.length);
                            registerDependentBeans(beanName, autowiredBeans);
                            if (autowiredBeans.size() == argumentCount) {
                                Iterator<String> it = autowiredBeans.iterator();
                                Class<?>[] paramTypes = method.getParameterTypes();
                                for (int i = 0; i < paramTypes.length; i++) {
                                    String autowiredBeanName = it.next();
                                    if (beanFactory.containsBean(autowiredBeanName) &&
                                            beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i])) {
                                        cachedMethodArguments[i] = new ShortcutDependencyDescriptor(
                                                descriptors[i], autowiredBeanName, paramTypes[i]);
                                    }
                                }
                            }
                            this.cachedMethodArguments = cachedMethodArguments;
                        } else {
                            this.cachedMethodArguments = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (arguments != null) {
                try {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }

        @Nullable
        private Object[] resolveCachedArguments(@Nullable String beanName) {
            Object[] cachedMethodArguments = this.cachedMethodArguments;
            if (cachedMethodArguments == null) {
                return null;
            }
            Object[] arguments = new Object[cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = resolvedCachedArgument(beanName, cachedMethodArguments[i]);
            }
            return arguments;
        }
    }


    /**
     * DependencyDescriptor variant with a pre-resolved target bean name.
     */
    @SuppressWarnings("serial")
    private static class ShortcutDependencyDescriptor extends DependencyDescriptor {

        private final String shortcut;

        private final Class<?> requiredType;

        public ShortcutDependencyDescriptor(DependencyDescriptor original, String shortcut, Class<?> requiredType) {
            super(original);
            this.shortcut = shortcut;
            this.requiredType = requiredType;
        }

        @Override
        public Object resolveShortcut(BeanFactory beanFactory) {
            return beanFactory.getBean(this.shortcut, this.requiredType);
        }
    }
}
