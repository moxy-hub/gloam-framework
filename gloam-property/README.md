# Gloam-Property【配置映射】

`gloam-property`是gloam框架核心依赖，gloam的后续框架接入都是以配置映射为基础，将所有的配置进行默认值的映射，避免使用框架后还需要考虑大量的配置。

## 🚀快速使用

**步骤**

- 创建spring的配置类，并在类上添加`@GloamConfigurationProperties`注解
- 在基础的配置上添加gloam的映射注解 --> `@MappingConfigurationProperty`
- 填写具体映射的值
- 启动项目，查看配置

**实例代码**

- 创建基础的spring配置类

