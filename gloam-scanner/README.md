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

包注册机制的实现核心是基于Spring框架的SPI机制，通过实现对应的接口，并将接口的实现在`spring.factory`中进行创建，如果您还不了解spring.factory机制，请先自行了解即可。

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

由于有时候我们可能只希望获取到一组class，但是GloamScanner扫描到的资源都是放在内存中，为了获取的时候直接拿取，而不是每次都循环筛选，所以就有了分组的功能。GloamScanner会在扫描资源时，以`@GloamResource`注解的`group`属性为分组的依据，会将相同一类的资源以`Map`的方式存放，随用随取。

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

考虑到我们可能需要自定义一些自己的注解，又想要将这个注解标注的资源扫描进去，那么第一想法，就是联合使用自定义注解和`@GloamResource`注解，将两个注解同时放在资源上，这样确实可以实现功能，但是却不是很优雅，所以`@GloamResource`同样支持标注在其他自定义注解上，那么此时的自定义注解将拥有和`@GloamResource`相同的功能，那么在使用时只需要标注您的自定义注解即可。

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





## 资源中心工厂