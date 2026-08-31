package com.swu2026.mydata_backend.config;

import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ReportApiConfig {

    private static final Logger log = LoggerFactory.getLogger(ReportApiConfig.class);

    @Bean
    public RestClient reportApiRestClient(@Value("${external.report-api.base-url}") String baseUrl) {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
            .requestInterceptor((request, body, execution) -> {
                log.info(
                    "[report-api] request {} {}\nbody: {}",
                    request.getMethod(), request.getURI(), new String(body, StandardCharsets.UTF_8)
                );
                ClientHttpResponse response = execution.execute(request, body);
                String responseBody = new String(
                    response.getBody().readAllBytes(), StandardCharsets.UTF_8
                );
                log.info(
                    "[report-api] response {}\nbody: {}", response.getStatusCode(), responseBody
                );
                return response;
            })
            .build();
    }
}
