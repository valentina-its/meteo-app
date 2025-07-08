package com.vale.meteo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.vale.meteo.config.MeteoApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(MeteoApiProperties.class)
public class MeteoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeteoApplication.class, args);
    }
}
