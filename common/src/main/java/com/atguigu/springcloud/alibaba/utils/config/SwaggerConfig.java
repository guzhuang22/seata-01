package com.atguigu.springcloud.alibaba.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger2.module.base-package:com.nrxt.aiict.controller}")
    private String basePackage;
    @Value("${swagger2.name:ict}")
    private String appName;
    @Value("${swagger2.website:www.ict.cn}")
    private String webSite;
    @Value("${swagger2.email:support@ict.cn}")
    private String email;
    @Value("${swagger2.module.title:commons api}")
    private String apiTitle;
    @Value("${swagger2.module.description:commons api}")
    private String apiDesc;
    @Value("${swagger2.module.serviceUrl:www.ict.cn}")
    private String serviceUrl;
    @Value("${swagger2.module.version:V2.92}")
    private String apiVersion;

    // TODO 线上取消
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(basePackage)).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(appName, "", "");
        return new ApiInfoBuilder().title(apiTitle).description(apiDesc)/*.termsOfServiceUrl(serviceUrl)*/.contact(contact)
                .version(apiVersion).build();
    }
}
