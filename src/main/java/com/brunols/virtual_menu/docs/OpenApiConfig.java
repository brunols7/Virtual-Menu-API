package com.brunols.virtual_menu.docs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Virtual Menu")
                        .description("A Virtual Menu API for Restaurants,")
                        .version("0.1")
                        .contact(new Contact()
                                .name("Bruno Leonardo")
                                .url("https://linkedin.com/in/brunols7")
                                .email("bruno.lsilva1508@gmail.com")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com.brunols.virtual_menu.controller")
                .pathsToMatch("/**")
                .build();
    }
}
