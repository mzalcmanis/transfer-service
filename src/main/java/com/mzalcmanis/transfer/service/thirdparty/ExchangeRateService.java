package com.mzalcmanis.transfer.service.thirdparty;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final RestTemplate exchangeRateRestTemplate;

    @Value("${exchange-rate-service.url}")
    private String url;

    @Cacheable("rates")
    public Map<String, BigDecimal> getRates(){
        try {
            log.info("Fetching rates");
            ResponseEntity<Response> response = exchangeRateRestTemplate.getForEntity(url, Response.class);
            return response.getBody().getRates();
        } catch (RestClientResponseException e) {
            String errorBody = e.getResponseBodyAsString();
            log.error("Rate API call error: " + errorBody, e);
            throw new ExchangeRateException("Failed fetching exchange rates from third-party service");
        } catch (RestClientException e) {
            log.error("Rate API error", e);
            throw new ExchangeRateException("Failed fetching exchange rates from third-party service");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class Response {
        String date;
        String base;
        Map<String, BigDecimal> rates;
    }
}
