package com.example.paper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Conf {

    @Bean
    public Docket getUserDocket(){
        ApiInfo apiInfo=new ApiInfoBuilder().title("Swagger2演示")//api标题
                        .description("API文档")
                        .version("1.0.0")
                        .contact("吴旻轩").build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.paper.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
