package com.javabycode.controller;

import com.javabycode.domain.WeatherType;
import com.javabycode.model.AjaxResponseBody;
import com.javabycode.model.WeatherRequest;
import com.javabycode.services.WeatherService;
import com.javabycode.services.WeatherServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class MyRestController {

    WeatherServiceFactory weatherServiceFactory;

    private static final Logger logger = LoggerFactory.getLogger(MyRestController.class);

    @Autowired
    public void setOpenWeatherService(WeatherServiceFactory weatherServiceFactory) {
        this.weatherServiceFactory = weatherServiceFactory;
    }

    @PostMapping("/api/weather")
    public ResponseEntity<?> getSearchResultViaAjax(@Valid @RequestBody WeatherRequest weatherRequest, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);

        }
        try {
            final Map<WeatherType, Double> typeToTemp = new HashMap<>();
            ArrayList<CompletableFuture<Double>> futures = new ArrayList();
            for (WeatherType weatherType : WeatherType.values()) {
                WeatherService weatherService = weatherServiceFactory.getWeatherService(weatherType);
                if (weatherService == null) {
                    logger.error("Weather type not supported : " + weatherType.getLabel());
                    continue;
                }
                futures.add(CompletableFuture.supplyAsync(() -> {
                    Double temperature = weatherService.getTemperature(weatherRequest);
                    String subResponse = "Temperature from " + weatherType.getLabel() + " is " + temperature;
                    logger.error(subResponse);
                    typeToTemp.put(weatherType, temperature);
                    return temperature;
                }));
            }
            final List<Double> temperatures = futures
                    .stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            double average = getAverage(temperatures);
            result.setMsg("success");
            final StringBuilder response = new StringBuilder();
            for (Map.Entry<WeatherType, Double> entry : typeToTemp.entrySet()) {
                response.append(entry.getKey().getLabel() + " : " + entry.getValue());
                response.append("; ");
            }
            response.append("Average Temperature : " + average);
            result.setResult(response.toString());
        } catch (Exception eX) {
            result.setMsg("API Error occurred : " + eX.getMessage());
        }

        return ResponseEntity.ok(result);

    }

    private double getAverage(List<Double> temperatures) {
        double average = 0;
        for (Double value : temperatures) {
            average += value;
        }
        average /= temperatures.size();
        return average;
    }
}
