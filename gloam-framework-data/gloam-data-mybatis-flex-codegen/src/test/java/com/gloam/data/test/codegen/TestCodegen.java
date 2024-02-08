package com.gloam.data.test.codegen;

import com.alibaba.druid.pool.DruidDataSource;
import com.gloamframework.data.mybatis.flex.codegen.MybatisFlexGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 晓龙
 */
public class TestCodegen {

    private MybatisFlexGenerator generator;
    private final String[] tablePrefix = {"tb_"};

    @Before
    public void before() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/Mybatis?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("19981104");
        generator = new MybatisFlexGenerator(dataSource);
    }

    @Test
    public void generate() {
        String[] tables = {"admin_info"};
        generator.generate(false, tablePrefix, "com.gloamframework.data.mybatis.flex.codegen", tables);
    }

}
