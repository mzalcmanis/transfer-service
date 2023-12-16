package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.service.thirdparty.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;

//TODO: which package?
@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    public static final MathContext MC_5DEC = new MathContext(5, RoundingMode.HALF_EVEN);
    public static final MathContext MC_2DEC = new MathContext(2, RoundingMode.HALF_EVEN);

    private final ExchangeRateService exchangeRateService;

    //TODO: test different conversions
    public Optional<BigDecimal> convert(BigDecimal amount, Currency sourceCurrency, Currency destCurrency) {
        if(sourceCurrency.equals(destCurrency)){
            return Optional.of(amount);
        }
        Map<String, BigDecimal> rates = exchangeRateService.getRates();
        BigDecimal sourceRate = rates.get(sourceCurrency.getCurrencyCode());
        BigDecimal destRate = rates.get(destCurrency.getCurrencyCode());
        if(sourceRate == null){
            log.error("No exchange rate found for {}", sourceCurrency);
            //TODO: exception or Result object?
            return Optional.empty();
        }
        if(destRate == null){
            log.error("No exchange rate found for {}", sourceCurrency);
            return Optional.empty();
        }

        BigDecimal convertedAmount = amount.divide(sourceRate, MC_5DEC)
                .multiply(destRate, MC_5DEC)
                .round(MC_2DEC);
        //TODO: debug level
        log.info("Conversion {} {} -> {} {}", amount, sourceCurrency, convertedAmount, destCurrency);

        return Optional.of(convertedAmount);
    }

}
