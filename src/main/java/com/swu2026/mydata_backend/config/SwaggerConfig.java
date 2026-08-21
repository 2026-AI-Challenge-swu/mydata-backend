package com.swu2026.mydata_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MyData Backend API")
                .description("연금 포트폴리오 추천 및 마이데이터 연동 API")
                .version("v0.0.1"));
    }
}
