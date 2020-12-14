package com.javabycode.domain;

import java.io.Serializable;
import java.util.List;

public class WeatherBitResponse implements Serializable {
    private Integer count;
    private List<WeatherBitChildResponse> data;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<WeatherBitChildResponse> getData() {
        return data;
    }

    public void setData(List<WeatherBitChildResponse> data) {
        this.data = data;
    }
}
