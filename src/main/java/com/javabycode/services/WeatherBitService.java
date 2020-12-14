package com.javabycode.services;

import com.javabycode.domain.WeatherBitResponse;
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
public class WeatherBitService implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherBitService.class);

    private static final String WEATHER_BIT_URL = "http://api.weatherbit.io/v2.0/current?city={city}&key={key}";
    
    private final RestTemplate restTemplate;

    private final String apiKey;

    public WeatherBitService(RestTemplateBuilder restTemplateBuilder,
                             WeatherAppProperties properties) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = properties.getApi().getWeatherBitKey();
    }

    @Override
    public WeatherType getType() {
        return WeatherType.WEATHER_BIT;
    }

    @Override
    public Double getTemperature(WeatherRequest weatherRequest) {
        logger.info("Requesting current weather for {}/{} from weather bit", weatherRequest.getCountry(), weatherRequest.getCity());
        URI url = new UriTemplate(WEATHER_BIT_URL).expand(weatherRequest.getCity(), apiKey);
        final WeatherBitResponse weatherBitResponse = invoke(url, WeatherBitResponse.class);
        if (weatherBitResponse == null || weatherBitResponse.getData() == null || weatherBitResponse.getData().size() == 0) {
            return 0d;
        }
        logger.error("Weather Bit Temperature : " + weatherBitResponse.getData().get(0).getTemp());
        return weatherBitResponse.getData().get(0).getTemp();
    }

    private <T> T invoke(URI url, Class<T> responseType) {
        RequestEntity<?> request = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<T> exchange = this.restTemplate
                .exchange(request, responseType);
        return exchange.getBody();
    }
}
