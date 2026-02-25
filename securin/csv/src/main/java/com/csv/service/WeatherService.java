package com.csv.service;

import com.csv.model.Weather;
import com.csv.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class WeatherService {

    private final WeatherRepository repository;

    public WeatherService(WeatherRepository repository) {
        this.repository = repository;
    }

    
    public List<Weather> getWeatherByDate(LocalDate date) {
        return repository.findByDate(date);
    }
    public Map<Integer, Map<String, Double>> getTemperatureStats(int year) {

        List<Weather> allData = repository.findAll();
        Map<Integer, List<Double>> monthTempMap = new HashMap<>();

        for (Weather w : allData) {
            if (w.getDate().getYear() == year) {
                int month = w.getDate().getMonthValue();
                monthTempMap
                        .computeIfAbsent(month, k -> new ArrayList<>())
                        .add(w.getTemperature());
            }
        }

        Map<Integer, Map<String, Double>> result = new HashMap<>();

        for (Map.Entry<Integer, List<Double>> entry : monthTempMap.entrySet()) {
            List<Double> temps = entry.getValue();
            Collections.sort(temps);

            Map<String, Double> stats = new HashMap<>();
            stats.put("min", temps.get(0));
            stats.put("max", temps.get(temps.size() - 1));
            stats.put("median", temps.get(temps.size() / 2));

            result.put(entry.getKey(), stats);
        }

        return result;
    }
}