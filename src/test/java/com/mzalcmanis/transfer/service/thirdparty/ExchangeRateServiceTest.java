package com.mzalcmanis.transfer.service.thirdparty;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@Slf4j
@SpringBootTest
class ExchangeRateServiceTest {

    @Autowired
    private ExchangeRateService service;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private RestTemplate exchangeRateRestTemplate;

    @BeforeEach
    protected void setUp() {
        cacheManager.getCacheNames()
                .forEach(name -> cacheManager.getCache(name).clear());
    }

    @Test
    void getRates_cacheOk() {
        Mockito.when(exchangeRateRestTemplate.getForEntity(Mockito.any(String.class), Mockito.eq(ExchangeRateService.Response.class)))
                .thenReturn(
                        ResponseEntity.ok(
                                ExchangeRateService.Response.builder()
                                        .rates(Map.of("USD", BigDecimal.ONE))
                                        .build()

                        )
                );
        Map<String, BigDecimal> rates = service.getRates();
        log.info(rates.toString());
        service.getRates();
        service.getRates();
        Mockito.verify(exchangeRateRestTemplate, times(1)).getForEntity(Mockito.any(String.class), Mockito.eq(ExchangeRateService.Response.class));
    }

    @Test
    void getRates_exception() {
        Mockito.when(exchangeRateRestTemplate.getForEntity(Mockito.any(String.class), Mockito.eq(ExchangeRateService.Response.class)))
                .thenThrow(new RestClientException("Foo exception"));
        assertThrows(ExchangeRateException.class, () -> service.getRates());
    }

    @Test
    void getRates_exceptionWithResponseBody() {
        Mockito.when(exchangeRateRestTemplate.getForEntity(Mockito.any(String.class), Mockito.eq(ExchangeRateService.Response.class)))
                .thenThrow(new RestClientResponseException("Foo exception", HttpStatus.BAD_REQUEST, "Status text", null, "response body".getBytes(), null));
        assertThrows(ExchangeRateException.class, () -> service.getRates());
    }
}