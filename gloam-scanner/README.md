# Gloam-Scanner【资源扫描器】

`gloam-scanner`在gloam框架中承担着重要的角色，通过特定的注解，将我们的Java Class读取到内存，在我们需要获取的时候，随时可以获取到这些Class进行使用。

## 🚀 快速使用

**步骤**

- 在需要的类上添加注解`@GloamResource`

- 通过工厂类获取资源中心 `ResourceCentre`

- 通过获取到的资源中心，获取匹配的类

**实例代码：**

- 需要扫描的目标class

  ```java
  package com.gloamframework.test.scanner.classes;
  import com.gloamframework.scanner.annotation.GloamResource;
  
  @GloamResource
  public class TestClass {
  }
  ```

- 获取资源class

  ```java
  public void testScannerResource() throws IOException {
      // 获取资源中心
      ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
      // 获取资源集合
      Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClasses(null);
      System.out.println(resourcesClasses);
  }
  ```

- 输出

  ```txt
  [class com.gloamframework.test.scanner.classes.TestClass]
  ```

## 包注册机制

包注册机制的实现核心是基于Spring框架的SPI机制，通过实现对应的接口，并将接口的实现在`spring.factory`
中进行创建，如果您还不了解spring.factory机制，请先自行了解即可。

**TIP:**

- 默认扫描的地址为`com.gloamframework`
- 如果您接入了`gloam-core`，则同时会添加SpringBoot的启动类所在路径之下

**实例代码：**

- 实现接口`com.gloamframework.scanner.ResourcePackage`

  ```java
  package com.gloamframework.scanner.packages;
  import com.gloamframework.scanner.ResourcePackage;
  
  
  public class GloamResourcePackage implements ResourcePackage {
  
      @Override
      public String[] register() {
          return new String[]{"输入您的包扫描路径"};
      }
  
  }
  ```

- 在`spring.factory`中注册SPI

  ```properties
  # 后面的值就是您对于接口的实现
  com.gloamframework.scanner.ResourcePackage = com.gloamframework.scanner.packages.GloamResourcePackage
  ```

## 资源分组

由于有时候我们可能只希望获取到一组class，但是GloamScanner扫描到的资源都是放在内存中，为了获取的时候直接拿取，而不是每次都循环筛选，所以就有了分组的功能。GloamScanner会在扫描资源时，以`@GloamResource`
注解的`group`属性为分组的依据，会将相同一类的资源以`Map`的方式存放，随用随取。

**TIP:**

- 如果在`@GloamResource`注解中没有设置`group`属性，则会将资源放在默认的组内，那么在获取资源的时候，分组参数也就无需传入
- 如果出现资源没有获取到的情况，请仔细核对注解`@GloamResource`的`group`属性是否和获取资源时传入的`group`一致

**实例代码：**

- 需要扫描的目标资源

  ```java
  package com.gloamframework.test.scanner.classes;
  import com.gloamframework.scanner.annotation.GloamResource;
  
  /**
   * 在资源扫描注解中，设置分组
   */
  @GloamResource(group = "myGroup")
  public class TestClass {
  }
  ```

- 获取资源class

  ```java
  public void testScannerResource() throws IOException {
      // 获取资源中心
      ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
      // 获取对应分组的class集合，分组参数和上面注解保持一致
      Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClasses("myGroup");
      System.out.println(resourcesClasses);
  }
  ```

- 输出

  ```txt
  [class com.gloamframework.test.scanner.classes.TestClass]
  ```

## 自定义注解

考虑到我们可能需要自定义一些自己的注解，又想要将这个注解标注的资源扫描进去，那么第一想法，就是联合使用自定义注解和`@GloamResource`
注解，将两个注解同时放在资源上，这样确实可以实现功能，但是却不是很优雅，所以`@GloamResource`
同样支持标注在其他自定义注解上，那么此时的自定义注解将拥有和`@GloamResource`相同的功能，那么在使用时只需要标注您的自定义注解即可。

**TIP:**

- 标注`@GloamResource`注解的自定义注解，是不会作为资源被添加到GloamScanner中
- 通过自定义注解，和资源中心的通过注解获取资源的方式，也可以实现基于注解的分组，但是这种获取会进行资源遍历，建议配合分组功能来缩小遍历范围

**实例代码:**

- 自定义注解

  ```java
  package com.gloamframework.test.scanner.annotation;
  
  import com.gloamframework.scanner.annotation.GloamResource;
  
  import java.lang.annotation.*;
  
  /**
   * 添加@GloamResource注解，获取资源扫描能力
   */
  @GloamResource
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface TestResourceAnno {
  }
  ```

- 使用自定义注解

  ```java
  package com.gloamframework.test.scanner.classes;
  
  import com.gloamframework.test.scanner.annotation.TestResourceAnno;
  
  /**
   * 使用自定义注解，注册资源
   */
  @TestResourceAnno
  public class TestClassWithAnno {
  }
  ```

- 【方式一】正常获取资源

  ```java
  public void testScannerResource() throws IOException {
      // 获取资源中心
      ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
      // 获取class集合
      Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClasses(null);
      System.out.println(resourcesClasses);
  }
  ```

- 【方式二】通过指定注解获取

  ```java
  public void testScannerResourceByAnnotation() throws IOException {
      // 获取资源中心
      ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
      // 获取标注了指定注解的class集合
      Set<Class<?>> resourcesClasses = resourceCentre.getResourcesClassesByAnnotation(
              null,
              TestResourceAnno.class
      );
      System.out.println(resourcesClasses);
  }
  ```

- 两种方式的输出

  ```txt
  [class com.gloamframework.test.scanner.classes.TestClassWithAnno]
  ```

## 资源中心

在上面的案例中，其实已经把资源中心功能进行了覆盖，资源中心，顾名思义就是通过当前接口可以获取到被扫描到的资源，主要分为两种获取模式，如何获取资源中心，请参考最后一章 `资源中心工厂`

- 默认获取资源

  > 获取到系统中的标注的@GloamResource注解的资源，并将资源加载为class
  >
  > 注：
  >
  > - 查询的资源全部都为class，使用本接口不会获取到class之外的资源

  > 参数：
  >
  > - group – 获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
      >
      >   ​           [如果不传入，可以直接调用重载方法]

  ```java
  /**
   * 获取到系统中的标注的@{@link GloamResource}注解的资源，并将资源加载为class
   * <p>Tip:
   * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
   * </p>
   *
   * @param group 获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
   */
  Set<Class<?>> getResourcesClasses(String group);
  
  /**
   * 获取到系统中的标注的@{@link GloamResource}注解的资源，并将资源加载为class
   * <p>Tip:
   * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
   * </p>
   */
  default Set<Class<?>> getResourcesClasses() {
      return this.getResourcesClasses(null);
  }
  
  ```


- 通过指定注解获取资源

  > 获取到系统中的标注的@GloamResource注解和指定的注解的资源，并将资源加载为class
  >
  > 注：
  >
  > - 查询的资源全部都为class，使用本接口不会获取到class之外的资源

  > 参数：
  >
  > - group – 获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
      >
      >   ​           [如果不传入，可以直接调用重载方法]
  >
  > - annotationClass – 资源class上绑定的其他注解

  ```java
  /**
   * 获取到系统中的标注的@{@link GloamResource}注解和指定的注解的资源，并将资源加载为class
   * <p>Tip:
   * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
   * </p>
   *
   * @param group           获取的资源分组，不传入则为默认的DEFAULT，需要和@GloamResource注解的group字段对应
   * @param annotationClass 资源class上绑定的其他注解
   * @see #getResourcesClasses(String)
   */
  Set<Class<?>> getResourcesClassesByAnnotation(String group, Class<? extends Annotation> annotationClass);
  
  /**
   * 获取到系统中的标注的@{@link GloamResource}注解和指定的注解的资源，并将资源加载为class
   * <p>Tip:
   * <li>查询的资源全部都为class，使用本接口不会获取到class之外的资源</li>
   * </p>
   *
   * @param annotationClass 资源class上绑定的其他注解
   * @see #getResourcesClasses(String)
   */
  default Set<Class<?>> getResourcesClassesByAnnotation(Class<? extends Annotation> annotationClass) {
      return getResourcesClassesByAnnotation(null, annotationClass);
  }
  ```

## 资源中心工厂

资源中心工厂是获取一个资源中心唯一的途径，当然您也可以实现资源中心接口，自定义您的资源扫描功能，如果是这样的话，那么资源中心工厂将毫无意义，因为它只能获取到默认的实现。

**资源中心工厂获取的资源中心是有两种方式**

- 单例获取

  > 优势：系统维护一个资源中心实例，系统只会扫描一次，减省系统的开销
  >
  > 缺点：因为获取时需要传入类加载器和日志系统，由于是单例模式，那么后续获取传入的值是不会被进行处理的

- 多实例获取

  > 优势：和单例相反，由于每次获取都是一个新的实例，那么每次的参数都是会生效的
  >
  > 缺点：同样，因为每次是新的实例，那么在实例化时就会进行资源扫描，如果有太多实例的话，可能会对系统的性能有影响

**参数详解**

- classLoader

  > 用于获取spring.factory和将资源加载为类的类加载器
  >
  > 默认为SpringFactoriesLoader.class.getClassLoader() 和ClassLoader.getSystemClassLoader()
  >
  > 如果外部传入，则统一使用外部的加载器

- log

  > 用于内部日志的输出
  >
  > 默认为LogFactory.getLog(String)获取的日志
  >
  > 为什么要外部传入日志，主要是考虑到在spring boot的env处理时，日志系统还未加载，可能需要使用到延迟的日志

**代码接口**

```java
package com.gloamframework.scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.Objects;

/**
 * 资源中心工程获取类，通过本工厂可以获取到系统默认的资源中心
 *
 * @author 晓龙
 * @see ResourceCentre
 */
public class ResourceCentreFactory {

    private static volatile ResourceCentre resourceCentre;

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为单例模式
     * 在第一次获取时传入的classLoader和log有效，后续的则无效
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     *
     * @param classLoader 获取spring.factory的类加载器，
     *                    默认为{@link SpringFactoriesLoader}.class.getClassLoader()
     *                    和{@link ClassLoader#getSystemClassLoader()}
     * @param log         外部传入的日志模块，主要为了方便延迟日志的实现，
     *                    默认为{@link LogFactory#getLog(String)}获取的日志
     */
    public static synchronized ResourceCentre ofSingleDefault(ClassLoader classLoader, Log log) throws IOException {
        if (Objects.isNull(resourceCentre)) {
            resourceCentre = ofDefault(classLoader, log);
        }
        return resourceCentre;
    }

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为单例模式
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     */
    public static ResourceCentre ofSingleDefault() throws IOException {
        return ofSingleDefault(null, null);
    }

    /**
     * 获取系统内置的默认实现的资源中心，获取到的资源中心为多例模式，每次调用都会创建新的资源中心
     * <p>
     * <b>Importance:</b>
     * 每次创建资源中心都会进行资源扫描，由于会产生额外的资源浪费，谨慎使用
     * </p>
     * <p>默认实现:{@link DefaultResourceCentre}</p>
     *
     * @param classLoader 获取spring.factory的类加载器，默认为{@link SpringFactoriesLoader}.class.getClassLoader()
     * @param log         外部传入的日志模块，主要为了方便延迟日志的实现，默认为{@link LogFactory#getLog(String)}获取的日志
     */
    public static ResourceCentre ofDefault(ClassLoader classLoader, Log log) throws IOException {
        if (Objects.isNull(log)) {
            log = LogFactory.getLog(ResourceCentreFactory.class);
        }
        return new DefaultResourceCentre(classLoader, log);
    }

}
```

