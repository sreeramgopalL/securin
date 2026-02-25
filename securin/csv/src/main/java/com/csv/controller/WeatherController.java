package com.csv.controller;

import com.csv.model.Weather;
import com.csv.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    
    @GetMapping("/date")
    public List<Weather> getByDate(@RequestParam String date) {
        return service.getWeatherByDate(LocalDate.parse(date));
    }

    @GetMapping("/temperature-stats/{year}")
    public Map<Integer, Map<String, Double>> temperatureStats(
            @PathVariable int year) {
        return service.getTemperatureStats(year);
    }
}