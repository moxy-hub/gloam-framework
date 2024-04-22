# Gloam-Scanner【资源扫描器】

`gloam-scanner`在gloam框架中承担着重要的角色，通过特定的注解，将我们的Java Class读取到内存，在我们需要获取的时候，随时可以获取到这些Class进行使用。

## 🚀 快速使用

**步骤**

- 在需要的类上添加注解`GloamResource`

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

- 获取class

  ```java
  public void testScannerResource() throws IOException {
      // 获取资源中心
      ResourceCentre resourceCentre = ResourceCentreFactory.ofSingleDefault();
      // 获取对应分组的class集合
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

## 资源中心

## 资源中心工厂