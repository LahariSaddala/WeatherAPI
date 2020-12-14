package com.javabycode.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Service
@ConfigurationProperties("app.weather")
public class WeatherAppProperties {

    @Valid
    private final Api api = new Api();

   private List<String> locations;

    public Api getApi() {
        return this.api;
    }

    public List<String> getLocations() {
        return this.locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public static class Api {

        /**
         * API key of the OpenWeatherMap service.
         */
        @NotNull
        private String openWeatherKey;

        /**
         * API key of the WeatherBit service.
         */
        @NotNull
        private String weatherBitKey;

        public String getOpenWeatherKey() {
            return this.openWeatherKey;
        }

        public void setOpenWeatherKey(String openWeatherKey) {
            this.openWeatherKey = openWeatherKey;
        }

        public String getWeatherBitKey() {
            return weatherBitKey;
        }

        public void setWeatherBitKey(String weatherBitKey) {
            this.weatherBitKey = weatherBitKey;
        }
    }

}
