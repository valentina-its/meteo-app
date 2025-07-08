package com.vale.meteo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient weatherClient(MeteoApiProperties props) {
        return WebClient.builder()
            .baseUrl(props.getBaseUrl())
            .build();
    }
}
