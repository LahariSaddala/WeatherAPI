package com.javabycode.services;

import com.javabycode.domain.WeatherType;
import com.javabycode.model.WeatherRequest;

public interface WeatherService {

    WeatherType getType();

    Double getTemperature(WeatherRequest weatherRequest);

}
