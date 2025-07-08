package com.vale.meteo.service;

import com.vale.meteo.model.WeatherResponse;

import reactor.core.publisher.Mono;

public interface ExternalApiService {

    Mono<String> ping();

    /** 
     * Restituisce l’intera mappa JSON, con chiavi:
     *  - "hourly" → Map<String,List<?>>
     *  - "daily"  → Map<String,List<?>>
     */
    Mono<WeatherResponse> fetchWeather(double lat, double lon);
}