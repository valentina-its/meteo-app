package com.vale.meteo.service;

import com.vale.meteo.model.WeatherResponse;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    private final WebClient client;

    public ExternalApiServiceImpl(WebClient weatherClient) {
        this.client = weatherClient;
    }

    @Override
    public Mono<String> ping() {
        return client.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("latitude", 0)
                .queryParam("longitude", 0)
                .queryParam("hourly", "temperature_2m")
                .queryParam("timezone", "UTC")
                .build())
            .retrieve()
            .bodyToMono(String.class); // Corretto: la risposta è una stringa grezza
    }

    @Override
    public Mono<WeatherResponse> fetchWeather(double lat, double lon) {
        return client.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("hourly", "temperature_2m")
                .queryParam("daily", "temperature_2m_max,temperature_2m_min")
                .queryParam("timezone", "Europe/Rome")
                .build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<WeatherResponse>() {});
    }
}
