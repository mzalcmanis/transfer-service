package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.service.impl.CurrencyServiceImpl;
import com.mzalcmanis.transfer.service.thirdparty.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    protected static final Currency USD = Currency.getInstance("USD");
    protected static final Currency EUR = Currency.getInstance("EUR");
    protected static final Currency NOK = Currency.getInstance("NOK");
    public static final BigDecimal RATE_EUR = new BigDecimal("0.9");
    public static final BigDecimal RATE_NOK = new BigDecimal("10.0");
    @InjectMocks
    CurrencyServiceImpl currencyService;

    @Mock
    ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp(){
        Mockito.lenient().when(exchangeRateService.getRates()).thenReturn(
                Map.of("USD", BigDecimal.ONE,
                        "EUR", RATE_EUR,
                        "NOK", RATE_NOK
                )
        );
    }

    @Test
    void convertSameCurrency() {
        assertThat(currencyService.convert(BigDecimal.TEN, USD, USD).get())
                .isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    void convertFromBase() {
        assertThat(currencyService.convert(BigDecimal.TEN, USD, EUR).get())
                .isEqualByComparingTo(BigDecimal.TEN.multiply(RATE_EUR, new MathContext(2)));
    }

    @Test
    void convertToBase() {
        assertThat(currencyService.convert(BigDecimal.TEN, EUR, USD).get())
                .isEqualByComparingTo(BigDecimal.TEN.divide(RATE_EUR, new MathContext(2)));
    }

    @Test
    void convertCrossPair() {
        assertThat(currencyService.convert(BigDecimal.TEN, EUR, NOK).get())
                .isEqualByComparingTo(BigDecimal.TEN
                        .divide(RATE_EUR, new MathContext(5))
                        .multiply(RATE_NOK, new MathContext(2))
                );
    }
}