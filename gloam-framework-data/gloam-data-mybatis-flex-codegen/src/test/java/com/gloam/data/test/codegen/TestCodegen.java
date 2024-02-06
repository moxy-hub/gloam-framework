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
        dataSource.setUrl("jdbc:mysql://192.168.1.136:49156/test_mybatis_flex?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("wdzh23frgs@isjfd2*^");
        generator = new MybatisFlexGenerator(dataSource);
    }

    @Test
    public void generate() {
        String[] tables = {"tb_account"};
        generator.generate(false, tablePrefix, "com.gloamframework.data.mybatis.flex.codegen", tables);
    }

}
