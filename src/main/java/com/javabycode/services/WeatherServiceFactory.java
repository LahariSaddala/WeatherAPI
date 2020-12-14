package com.javabycode.services;

import com.javabycode.domain.WeatherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceFactory {

    private static final Map<WeatherType, WeatherService> WEATHER_TYPE_TO_SERVICE = new HashMap<>();

    @Autowired
    public void WeatherServiceFactory(List<WeatherService> weatherServices) {
        for (WeatherService weatherService : weatherServices) {
            WEATHER_TYPE_TO_SERVICE.put(weatherService.getType(), weatherService);
        }
    }

    public WeatherService getWeatherService(WeatherType weatherType) {
        return WEATHER_TYPE_TO_SERVICE.get(weatherType);
    }

}
