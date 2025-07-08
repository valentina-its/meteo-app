package com.vale.meteo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vale.meteo.service.ExternalApiService;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/")
public class MeteoController {

    private final ExternalApiService svc;

    private static final DateTimeFormatter HOUR_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DAY_FMT  = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final ZoneId ZONE = ZoneId.of("Europe/Rome");

    public MeteoController(ExternalApiService svc) {
        this.svc = svc;
    }

    @GetMapping
    public String meteoPage() {
        return "meteo";  
    }

    @GetMapping(value = "weather/chart", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<byte[]>> chart(@RequestParam double lat, @RequestParam double lon) {
        return svc.fetchWeather(lat, lon).map(full -> {

            // Accedo alle mappe direttamente dai metodi del record
            Map<String, List<?>> rawHourly = full.hourly();
            Map<String, List<?>> rawDaily  = full.daily();

            // Converto i dati orari
            List<String> timesH = rawHourly.get("time").stream().map(Object::toString).collect(Collectors.toList());
            List<Double> valsH  = rawHourly.get("temperature_2m").stream().map(o -> ((Number) o).doubleValue()).collect(Collectors.toList());

            // Converto i dati giornalieri
            List<String> timesD = rawDaily.get("time").stream().map(Object::toString).collect(Collectors.toList());
            List<Double> maxD   = rawDaily.get("temperature_2m_max").stream().map(o -> ((Number) o).doubleValue()).collect(Collectors.toList());
            List<Double> minD   = rawDaily.get("temperature_2m_min").stream().map(o -> ((Number) o).doubleValue()).collect(Collectors.toList());

            // Serie oraria
            TimeSeries tsH = new TimeSeries("Temperatura Oraria");
            for (int i = 0; i < timesH.size(); i++) {
                LocalDateTime dt = LocalDateTime.parse(timesH.get(i), HOUR_FMT).atZone(ZONE).toLocalDateTime();
                tsH.add(new Hour(dt.getHour(), dt.getDayOfMonth(), dt.getMonthValue(), dt.getYear()), valsH.get(i));
            }

            // Serie giornaliere
            TimeSeries tsMax = new TimeSeries("Temperatura Max Giornaliera");
            TimeSeries tsMin = new TimeSeries("Temperatura Min Giornaliera");
            for (int i = 0; i < timesD.size(); i++) {
                LocalDate d = LocalDate.parse(timesD.get(i), DAY_FMT);
                Day day = new Day(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
                tsMax.add(day, maxD.get(i));
                tsMin.add(day, minD.get(i));
            }

            // Costruisco il dataset e il grafico
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(tsH);
            dataset.addSeries(tsMax);
            dataset.addSeries(tsMin);

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Temperatura Oraria e Giornaliera",
                "Data/Ora", "°C",
                dataset, true, false, false
            );

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ChartUtils.writeChartAsPNG(out, chart, 800, 500);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(out.toByteArray());
            } catch (IOException e) {
                return ResponseEntity.status(500).build();
            }
        });
    }
}
