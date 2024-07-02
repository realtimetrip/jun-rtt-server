package com.bbangjun.realtimetrip.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("RealTimeTrip API")
                        .description("RealTimeTrip Application API documentation")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("RealTimeTrip bbangjun server Github")
                        .url("https://github.com/realtimetrip/jun-rtt-server"));
    }
}
