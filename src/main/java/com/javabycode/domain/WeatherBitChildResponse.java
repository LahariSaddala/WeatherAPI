package com.javabycode.domain;

import java.io.Serializable;

public class WeatherBitChildResponse implements Serializable {
    private Double temp;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
