package com.javabycode.domain;

public enum WeatherType {
    OPEN_WEATHER("Open Weather"),
    WEATHER_BIT("Weather Bit");

    String label;

    private WeatherType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
