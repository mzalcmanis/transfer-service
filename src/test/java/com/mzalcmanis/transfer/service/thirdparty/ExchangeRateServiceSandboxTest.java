package com.mzalcmanis.transfer.service.thirdparty;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Disabled("Performs actual API calls to the external service, meant for development purposes")
@SpringBootTest
class ExchangeRateServiceSandboxTest {

    @Autowired
    private ExchangeRateService service;

    @Test
    void getRates() {
        Map<String, BigDecimal> rates = service.getRates();
//        log.info(rates.toString());
    }
}