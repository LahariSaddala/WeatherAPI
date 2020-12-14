package com.javabycode.model;

import org.hibernate.validator.constraints.NotBlank;

public class WeatherRequest {

    @NotBlank(message = "Zipcode can't empty!")
    String country;

    @NotBlank(message = "city can't empty!")
    String city;

    
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public void setCity(String city) {
		this.city = city;
	}
    
    public String getCity() {
		return city;
	}
    
}