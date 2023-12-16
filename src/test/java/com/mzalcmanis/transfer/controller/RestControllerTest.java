package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.db.entity.AccountEntity;
import com.mzalcmanis.transfer.db.entity.ClientEntity;
import com.mzalcmanis.transfer.db.repo.AccountRepository;
import com.mzalcmanis.transfer.db.repo.ClientRepository;
import com.mzalcmanis.transfer.service.thirdparty.ExchangeRateService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestControllerTest {

    protected static final Currency USD = Currency.getInstance("USD");
    protected static final Currency EUR = Currency.getInstance("EUR");
    protected static final Currency NOK = Currency.getInstance("NOK");
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @MockBean
    ExchangeRateService exchangeRateService;

    @LocalServerPort
    protected Integer localPort;
    protected ClientEntity client;
    protected ClientEntity client2;
    protected AccountEntity accUsd;
    protected AccountEntity accEur;
    protected AccountEntity accNok;
    protected AccountEntity accClient2Usd;
    protected AccountEntity accClient2Eur;
    protected AccountEntity accClient2Nok;
    protected ClientEntity clientNoAccounts;

    @BeforeEach
    void setUp() {

        RestAssured.port = localPort;

        Mockito.when(exchangeRateService.getRates()).thenReturn(
                Map.of("USD", BigDecimal.ONE,
                        "EUR", new BigDecimal("0.9188603300781981"),
                        "NOK", new BigDecimal("10.776833333333334"),
                        "CZK", new BigDecimal("22.453598675508598"),
                        "JPY", new BigDecimal("142.81116666666668")
                )
        );

        generateCommonTestData();

    }

    private void generateCommonTestData() {
        client = clientRepository.save(new ClientEntity(null, "Jack", "Johnson"));
        client2 = clientRepository.save(new ClientEntity(null, "Anna", "Bell"));
        clientNoAccounts = clientRepository.save(new ClientEntity(null, "Henry", "Green"));

        accUsd = accountRepository.save(new AccountEntity(null, client.getId(), "IBAN0001", USD, new BigDecimal(100)));
        accEur = accountRepository.save(new AccountEntity(null, client.getId(), "IBAN0002", EUR, new BigDecimal(100)));
        accNok = accountRepository.save(new AccountEntity(null, client.getId(), "IBAN0003", NOK, new BigDecimal(100)));
        accClient2Usd = accountRepository.save(new AccountEntity(null, client2.getId(), "IBAN0004", USD, new BigDecimal(200)));
        accClient2Eur = accountRepository.save(new AccountEntity(null, client2.getId(), "IBAN0005", EUR, new BigDecimal(200)));
        accClient2Nok = accountRepository.save(new AccountEntity(null, client2.getId(), "IBAN0006", NOK, new BigDecimal(200)));
    }

}