package com.gloamframework.web.doc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


public class HttpDocDocket extends Docket {

    public HttpDocDocket(String title, String apiName, String basePackage, String version,
                         String leader, String description, String serviceUrl) {
        super(DocumentationType.SWAGGER_2);
        this.groupName(apiName)
                .useDefaultResponseMessages(false)
                .apiInfo(new ApiInfoBuilder()
                        .title(title)
                        .description(description)
                        .termsOfServiceUrl(serviceUrl)
                        .version(version)
                        .contact(new Contact(leader, "", ""))
                        .build())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }
}
