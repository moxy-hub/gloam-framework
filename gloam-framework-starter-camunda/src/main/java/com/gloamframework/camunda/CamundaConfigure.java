package com.gloamframework.camunda;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.gloamframework.camunda.properties.CamundaProperties;
import com.gloamframework.data.properties.DataSourceProperties;
import com.gloamframework.data.properties.GloamDataProperties;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.spring.boot.starter.event.ProcessApplicationStartedEvent;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.camunda.bpm.spring.boot.starter.property.WebappProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author 晓龙
 */
@Slf4j
@Configurable
@EnableConfigurationProperties(CamundaProperties.class)
public class CamundaConfigure {

    @Autowired
    private CamundaBpmProperties camundaBpmProperties;

    @Autowired
    private ServerProperties serverProperties;

    @EventListener
    public void startup(ProcessApplicationStartedEvent ignore) throws UnknownHostException {
        WebappProperty webapp = camundaBpmProperties.getWebapp();
        InetAddress address = serverProperties.getAddress();
        if (address == null || StrUtil.isBlank(address.getHostAddress())) {
            address = InetAddress.getLocalHost();
        }
        String baseURL = address.getHostAddress() + ":" + serverProperties.getPort();
        String visitURL = baseURL + "/" + webapp.getApplicationPath() + "/app/#/login";
        log.info("camunda web app start at [ {} ], url to visit: {}", webapp.getApplicationPath(), URLUtil.normalize(visitURL));
    }

    @Bean(name = "camundaBpmDataSource")
    public DataSource camundaBpmDataSource(GloamDataProperties gloamDataProperties) {
        DataSourceProperties masterDatasource = gloamDataProperties.getMasterDatasource();
        return DataSourceBuilder
                .create()
                .url(masterDatasource.getUrl())
                .driverClassName(masterDatasource.getDriverClassName())
                .type(masterDatasource.getType())
                .password(masterDatasource.getPassword())
                .username(masterDatasource.getUsername())
                .build();
    }

    @Bean(name = "camundaBpmTransactionManager")
    public PlatformTransactionManager camundaTransactionManager(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public CamundaDeploymentService camundaDeploymentService(RepositoryService repository) {
        return new DefaultCamundaDeploymentService(repository);
    }

}
