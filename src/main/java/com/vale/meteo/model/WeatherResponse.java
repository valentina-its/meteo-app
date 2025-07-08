package com.vale.meteo.model;

import java.util.List;
import java.util.Map;

public record WeatherResponse(
    Map<String, List<?>> hourly,
    Map<String, List<?>> daily
) {}
