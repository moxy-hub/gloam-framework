package com.gloamframework.camunda.properties;

import com.gloamframework.core.boot.properties.annotation.MappingConfigurationProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * camunda配置
 *
 * @author 晓龙
 */
@ConfigurationProperties("gloam.camunda")
@Data
public class CamundaProperties {

    /**
     * 是否开启自动部署resource中的bpmn文件
     */
    @MappingConfigurationProperty("camunda.bpm.auto-deployment-enabled")
    private boolean autoDeploymentEnabled = true;

    /**
     * webapp用户配置
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("camunda.bpm.admin-user")
    private AdminUser user = new AdminUser();

    /**
     * webApp配置
     */
    @NestedConfigurationProperty
    @MappingConfigurationProperty("camunda.bpm.webapp")
    private WebApp webApp = new WebApp();

    @NestedConfigurationProperty
    @MappingConfigurationProperty("camunda.bpm.database")
    private Database database = new Database();

    @MappingConfigurationProperty("camunda.bpm.filter.create")
    private String filterCreate = "allTask";

    @Data
    public static class AdminUser {

        /**
         * 登录用户id
         */
        @MappingConfigurationProperty("id")
        private String id = "gloam";

        /**
         * 登录用户姓名
         */
        @MappingConfigurationProperty("first-name")
        private String firstName = "moxy";

        /**
         * 登录用户姓名
         */
        @MappingConfigurationProperty("last-name")
        private String lastName = "gloam";

        /**
         * 登录用户邮箱
         */
        @MappingConfigurationProperty("email")
        private String email = "moxy.mxl@gmail.com";

        /**
         * 登录用户密码
         */
        @MappingConfigurationProperty("password")
        private String password = "gloam";
    }

    @Data
    public static class WebApp {

        /**
         * 默认webapp访问地址
         */
        @MappingConfigurationProperty("application-path")
        private String applicationPath = "/gloam/camunda";
    }

    @Data
    public static class Database {
        /**
         * the database type
         */
        @MappingConfigurationProperty("type")
        private String type = "mysql";
    }
}
