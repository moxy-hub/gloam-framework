# Gloam-Property【配置映射】

`gloam-property`是gloam框架核心依赖，gloam的后续框架接入都是以配置映射为基础，将所有的配置进行默认值的映射，避免使用框架后还需要考虑大量的配置。

## 🚀快速使用

**步骤**

- 创建spring的配置类，并在类上添加`@MappingConfigurationProperties`注解
- 在基础的配置上添加gloam的映射注解 --> `@MappingConfigurationProperty`
- 填写具体映射的值
- 启动项目，查看配置

**实例代码**

- 我们准备两个相同的spring配置类进行测试

  **SourceProperties**

  ```java
  @ConfigurationProperties("gloam.source")
  @Data
  public class SourceProperties {
  
      private String testMapping;
  
  }
  ```

  **MappingProperties**

  ```java
  @ConfigurationProperties("gloam.mapping")
  @Data
  public class MappingProperties {
  
      private String testMapping;
  
  }
  ```

  **TIP**

  > - 两个配置类的属性名字全部一样，我们通过不同的类名进行区分，通过不同的配置前缀注册到spring中
  >
  > - 关于如何将配置在spring中激活这里不进行展示，相关问题可以直接百度


-

现在我们希望把source类的配置映射在mapping类中，让两个类进行关联，使得我们修改source类的配置，则mapping类中会进行生效，所以此时我们注解的作用就来了，因为我们是希望将source的配置映射在mapping中，所以我们只需要在source类中进行配置

- 在source类上添加注解`@MappingConfigurationProperties`
  ，用来标记当前配置是需要进行映射的，并在需要映射的属性上添加注解 `@MappingConfigurationProperty`
  来标记当前属性需要进行映射，一个简单的映射就此结束

  ```java
  /**
   * 添加@MappingConfigurationProperties注解，并指定前缀为映射的前缀
   */
  @MappingConfigurationProperties("gloam.mapping")
  @ConfigurationProperties("gloam.source")
  @Data
  public class SourceProperties {
  
      /**
       * 添加@MappingConfigurationProperty表示当前属性进行映射
       */
      @MappingConfigurationProperty
      private String testMapping;
  
  }
  ```

- 【在gloam环境下无需配置】如何让我们的配置生效？在我们的gloam-core中对配置映射进行了统一的启动，我们可以直接使用注解，但是如果您只想单独使用property这个功能，而不想去引入更多的依赖，那么可以参考下面的实例化方案

  ```java
  /**
   * 使用spring的EnvironmentPostProcessor进行处理，因为我们映射的是spring的配置，优先级要在处理环境时进行
   * 由于在环境初始化时，日志系统还没有准备好，我们又希望将日志输出，所以使用springboot为我们提供的延迟日志
   * 配合spring的事件系统，在监听到环境准备完成的事件后，对我们的日志进行回放
   */
  public class SpringEnvPost implements EnvironmentPostProcessor, ApplicationListener<ApplicationEnvironmentPreparedEvent> {
  
      /**
       * spring boot延迟日志
       */
      private static final DeferredLog deferredLog = new DeferredLog();
  
      @Override
      public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
          // 实例化配置映射，将日志传入用于内部日志的打印，将spring的环境传入，命名映射后配置在环境中存储的namespace，传入类加载器
          PropertyMapper propertyMapper = new DefaultPropertyMapper(deferredLog, environment, "test-mapping", application.getClassLoader());
          // 执行映射
          propertyMapper.mapping();
      }
  
      @Override
      public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
          // 在监听到环境准备就绪事件后进行日志回放
          deferredLog.replayTo(SpringEnvPost.class);
      }
  }
  ```

  **TIP**

  > 对于EnvironmentPostProcessor和ApplicationListener的配置，这里不进行展示，可以自行查阅springboot相关资料

- 接下来我们创建一个测试类，测试一下我们的功能

  ```java
  @SpringBootTest
  @RunWith(SpringRunner.class)
  public class TestPropertyMapper {
  
      @Autowired
      private SourceProperties sourceProperties;
  
      @Autowired
      private MappingProperties mappingProperties;
  
      @Test
      public void testMapping(){
          // 先检查两个类的值是否一致，如果成功，则代表映射成功
          Assert.assertEquals("映射失败",sourceProperties.getTestMapping(),mappingProperties.getTestMapping());
          // 同样我们可以将映射类的值打印出来看看
          System.out.println(mappingProperties.getTestMapping());
      }
  }
  ```

-

我们在编写完测试类后，对source的值进行修改试试，可以通过application.properties进行设置，或者在类中直接给默认值，在我们相关启动后，source类的配置总能映射到mapping类中，我们在项目中可以通过mapping类进行获取

## 在Spring中的作用

## 接入方式

### gloam框架

### 其他框架

## 注解

### MappingConfigurationProperties

### MappingConfigurationProperty

### 注解配合实现自由映射

## 复杂类型的映射

### Map对象

### Collection对象

### 数组对象

### 自定义对象

## 扩展-配置转换器

### 内置转换器

### 自定义实现转换器

### 通过注解注册转换器

## 常见问题

### 映射失效

如果在使用的过程中出现了映射失效的问题，可以参考下面两个方向进行排查：

- **映射路径错误**

  > 请检查映射的路径是否正确，包括类上的配置前缀，最终生成的配置路径是否正确
  >
  > 您可以通过将日志级别调制trace，在项目启动后，在spring banner打印前，会将映射的路径信息进行打印

- **GloamScanner配置**

  >
  gloam-property的实现基于gloam-scanner扫描机制，如果您的映射没有生效，可能是gloam-scanner的扫描机制并没有扫描到您的包，gloam-scanner默认的扫描范围为com.gloamframework，当然如果您使用的是gloam框架，默认的扫描地址还会添加spring
  boot启动类所对应的包。
  >
  > 如果您在使用gloam-property封装starter，那么切记是需要注册gloam-scanner的包扫描，相关具体方式可参考gloam-scanner文档

