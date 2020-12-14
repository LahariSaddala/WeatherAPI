package com.javabycode.services;

import com.javabycode.domain.OpenWeatherResponse;
import com.javabycode.domain.WeatherType;
import com.javabycode.model.WeatherRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@Service
public class OpenWeatherService implements WeatherService {

    private static final String OPEN_WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/weather?q={city},{country}&APPID={key}";


    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherService.class);

    private final RestTemplate restTemplate;

    private final String apiKey;

    public OpenWeatherService(RestTemplateBuilder restTemplateBuilder,
                              WeatherAppProperties properties) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = properties.getApi().getOpenWeatherKey();
    }

    @Override
    public WeatherType getType() {
        return WeatherType.OPEN_WEATHER;
    }

    @Override
    public Double getTemperature(WeatherRequest weatherRequest) {
        logger.info("Requesting current weather for {}/{} from open weather", weatherRequest.getCity(), weatherRequest.getCountry());
        URI url = new UriTemplate(OPEN_WEATHER_URL).expand(weatherRequest.getCity(), weatherRequest.getCountry(), this.apiKey);
        return invoke(url, OpenWeatherResponse.class).getCelsiusTemperature();
    }


    private <T> T invoke(URI url, Class<T> responseType) {
        RequestEntity<?> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<T> exchange = this.restTemplate
                .exchange(request, responseType);
        return exchange.getBody();
    }
}
