package com.altamirano_pajares.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("T2 SOLWEB 16046 - Altamirano / Pajares")
                .description("Caso 1 Teacher (Altamirano Sanchez Jean Pierre) y Caso 14 Tournament (Pena Pajares Jhon Paolo)")
                .version("1.0"));
    }
}
