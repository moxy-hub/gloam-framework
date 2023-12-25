package com.gloamframework.data.mybatis.flex.codegen;

import cn.hutool.core.util.StrUtil;
import com.gloamframework.data.mybatis.flex.domain.BaseDaoDomain;
import com.gloamframework.data.mybatis.flex.listener.GloamInsertListener;
import com.gloamframework.data.mybatis.flex.listener.GloamUpdateListener;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.config.TableConfig;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码生成器
 *
 * @author 晓龙
 */
@AllArgsConstructor
public class MybatisFlexGenerator {

    /**
     * 数据源
     */
    private final DataSource dataSource;

    private final static String DEFAULT_PACKAGE = ".datastore";

    public void generate(String modulePackage, String[] tables) {
        generate(new String[]{}, modulePackage, tables);
    }

    public void generate(String[] tablePrefix, String modulePackage, String[] tables) {
        generate(false, tablePrefix, modulePackage, tables);
    }

    public void generate(boolean enableOverride, String[] tablePrefix, String modulePackage, String[] tables) {
        generate(true, true, enableOverride, tablePrefix, modulePackage, tables);
    }

    public void generate(boolean enableSwagger2, boolean enableLombok, boolean enableOverride, String[] tablePrefix, String modulePackage, String[] tables) {
        // 生成代码
        Generator generator = new Generator(this.dataSource, createGlobalConfig(enableSwagger2, enableLombok, enableOverride, tablePrefix, modulePackage, tables));
        List<Table> generatorTables = generator.getTables();
        // 设置内容填充
        generatorTables.forEach(table -> {
            TableConfig tableConfig = new TableConfig();
            tableConfig.setInsertListenerClass(GloamInsertListener.class);
            tableConfig.setUpdateListenerClass(GloamUpdateListener.class);
            table.setTableConfig(tableConfig);
            // 对继承的字段进行删除
            table.setColumns(this.clearColumns(table.getColumns(), BaseDaoDomain.class));
        });
        generator.generate(generatorTables);
    }

    private List<Column> clearColumns(List<Column> columns, Class<?> baseClass) {
        Set<String> allGenField = FieldUtils.getAllFieldsList(baseClass).stream().map(field -> this.convertFieldName2DBName(field.getName())).collect(Collectors.toSet());
        return columns.stream()
                .filter(column -> {
                    String columnName = column.getName();
                    return !allGenField.contains(columnName);
                }).
                peek(column -> {
                    // 处理时间类型
                    if (StrUtil.equalsIgnoreCase("java.sql.Timestamp", column.getPropertyType())) {
                        column.setPropertyType("java.time.LocalDateTime");
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 将驼峰转换为"_"模式
     */
    private String convertFieldName2DBName(String fieldName) {
        fieldName = fieldName.replaceAll("([A-Z])", "_$1").toLowerCase();
        return StrUtil.startWith(fieldName, "_") ? StrUtil.subSuf(fieldName, 1) : fieldName;
    }

    private GlobalConfig createGlobalConfig(boolean enableSwagger2, boolean enableLombok, boolean enableOverride, String[] tablePrefix, String modulePackage, String[] tables) {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();
        // 设置表前缀和只生成哪些表
        globalConfig.setTablePrefix(tablePrefix);
        globalConfig.setGenerateTable(tables);
        // 设置生成路径
        globalConfig.getPackageConfig()
                .setBasePackage(modulePackage + DEFAULT_PACKAGE)
                .setEntityPackage(globalConfig.getBasePackage() + ".entity")
                .setMapperPackage(globalConfig.getBasePackage() + ".mapper")
                .setTableDefPackage(globalConfig.getBasePackage() + ".table")
                .setMapperXmlPath(this.getXMLPath(globalConfig));
        // entity配置
        globalConfig.enableEntity()
                // 设置时候使用lombok
                .setWithLombok(enableLombok)
                // 是否启动swagger
                .setWithSwagger(enableSwagger2)
                // 设置是否重写覆盖
                .setOverwriteEnable(enableOverride)
                // 设置继承类
                .setSuperClass(BaseDaoDomain.class);
        // mapper配置
        globalConfig.enableMapper().setOverwriteEnable(enableOverride);
        // xml配置
        globalConfig.enableMapperXml().setOverwriteEnable(enableOverride);
        // tableRef配置
        globalConfig.enableTableDef().setClassSuffix("Table").setOverwriteEnable(enableOverride);
        return globalConfig;
    }

    private String getXMLPath(GlobalConfig globalConfig) {
        String xmlPath = globalConfig.getSourceDir() + "." + globalConfig.getMapperPackage() + ".xml";
        xmlPath = xmlPath.replaceAll("\\.", "/");
        return xmlPath;
    }
}
