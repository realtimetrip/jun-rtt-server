package com.bbangjun.realtimetrip.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Real Time Trip API")
                        .description("Real-Time Trip Application API documentation")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Real-Time Trip Wiki Documentation")
                        .url("https://github.com/realtimetrip/jun-rtt-server"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/auth/**")
                .build();
    }
}
