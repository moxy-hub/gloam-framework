package com.gloamframework.data.mybatis.flex.codegen;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;

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
        Generator generator = new Generator(this.dataSource, createGlobalConfig(enableSwagger2, enableLombok, enableOverride, tablePrefix, modulePackage, tables));
        //生成代码
        generator.generate();
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
                .setOverwriteEnable(enableOverride);
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
