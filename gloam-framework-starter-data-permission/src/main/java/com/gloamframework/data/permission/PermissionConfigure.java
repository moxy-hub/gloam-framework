package com.gloamframework.data.permission;

import com.gloamframework.data.mybatis.flex.interceptor.FlexInterceptor;
import com.gloamframework.data.mybatis.flex.interceptor.MybatisFlexInterceptor;
import com.gloamframework.data.permission.aop.DataPermissionAnnotationAdvisor;
import com.gloamframework.data.permission.db.DataPermissionDatabaseInterceptor;
import com.gloamframework.data.permission.rule.DataPermissionRule;
import com.gloamframework.data.permission.rule.DataPermissionRuleFactory;
import com.gloamframework.data.permission.rule.DataPermissionRuleFactoryImpl;
import com.gloamframework.data.permission.rule.dept.DeptDataPermissionRule;
import com.gloamframework.data.permission.rule.dept.DeptDataPermissionRuleCustomizer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 数据权限配置
 *
 * @author 晓龙
 */
@Configurable
public class PermissionConfigure {

    @Bean
    public DeptDataPermissionRule deptDataPermissionRule(List<DeptDataPermissionRuleCustomizer> customizers) {
        // 创建 DeptDataPermissionRule 对象
        DeptDataPermissionRule rule = new DeptDataPermissionRule();
        // 补全表配置
        customizers.forEach(customizer -> customizer.customize(rule));
        return rule;
    }

    @Bean
    public DataPermissionRuleFactory dataPermissionRuleFactory(List<DataPermissionRule> rules) {
        return new DataPermissionRuleFactoryImpl(rules);
    }

    @Bean
    public FlexInterceptor DataPermissionDatabaseInterceptor(MybatisFlexInterceptor mybatisFlexInterceptor, DataPermissionRuleFactory ruleFactory) {
        DataPermissionDatabaseInterceptor dataPermissionDatabaseInterceptor = new DataPermissionDatabaseInterceptor(ruleFactory);
        mybatisFlexInterceptor.addInnerInterceptor(dataPermissionDatabaseInterceptor);
        return dataPermissionDatabaseInterceptor;
    }

    @Bean
    public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
        return new DataPermissionAnnotationAdvisor();
    }
}
