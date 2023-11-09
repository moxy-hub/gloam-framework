package com.gloamframework.data.mybatis.flex.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * mybatisFlex配置项
 *
 * @author 晓龙
 */
@Data
@ConfigurationProperties("gloam.data.mybatis.flex")
public class MybatisFlexProperties {

    /**
     * 默认的xml支持，不支持在spring中修改
     */
    @MappingConfigurationProperty("mybatis-flex.mapper-locations")
    private final String XML_SUPPORT = "classpath*:/mapper/**/*.xml,classpath*:/com/**/mapper/xml/*.xml";

    /**
     * mapper接口扫描的基础包路径,默认com.gloam.**.mapper
     * 建议mapper结尾，因为系统中内置http也是接口方式，不指定mapper会导致mybatis将http的接口类进行代理
     */
    private String mapperScanPackage = "com.gloam.**.mapper";

    /**
     * 主键配置方式
     * 全局的 ID 生成策略配置，当 @Id 未配置或者配置 KeyType 为 None 时 使用当前全局配置。
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("mybatis-flex.global-config.key-config")
    private final KeyProperties key = new KeyProperties();

    /**
     * seate分布式事务支持
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("mybatis-flex.seata-config")
    private final SeataProperties seata = new SeataProperties();

    /**
     * 逻辑删除数据存在标记值，默认为0
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.normal-value-of-logic-delete")
    private Object normalValueOfLogicDelete = 0;

    /**
     * 逻辑删除数据删除标记值，默认为1
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.deleted-value-of-logic-delete")
    private Object deletedValueOfLogicDelete = 1;

    /**
     * 默认的逻辑删除字段,默认为del
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.logic-delete-column")
    private String logicDeleteColumn = "del";

    /**
     * 默认的多租户字段,默认为tenant_id
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.tenant-column")
    private String tenantColumn = "tenant_id";

    /**
     * 默认的乐观锁字段，默认为version
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.version-column")
    private String versionColumn = "version";

    /**
     * 默认分页大小,默认为10
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.default-page-size")
    private int defaultPageSize = 10;

    /**
     * 递归关系查询深度，默认为5
     */
    @MappingConfigurationProperty("mybatis-flex.global-config.default-relation-query-depth")
    private int defaultRelationQueryDepth = 5;
}
