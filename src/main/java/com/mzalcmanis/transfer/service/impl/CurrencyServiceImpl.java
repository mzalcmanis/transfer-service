package com.mzalcmanis.transfer.service.impl;

import com.mzalcmanis.transfer.api.ApiResult;
import com.mzalcmanis.transfer.service.CurrencyService;
import com.mzalcmanis.transfer.service.thirdparty.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    public static final MathContext MC_5DEC = new MathContext(5, RoundingMode.HALF_EVEN);
    public static final MathContext MC_2DEC = new MathContext(2, RoundingMode.HALF_EVEN);

    private final ExchangeRateService exchangeRateService;

    //TODO: test different conversions
    @Override
    public ApiResult<BigDecimal> convert(BigDecimal amount, Currency sourceCurrency, Currency destCurrency) {
        if(sourceCurrency.equals(destCurrency)){
            return ApiResult.ofSuccess(amount);
        }
        Map<String, BigDecimal> rates = exchangeRateService.getRates();
        BigDecimal sourceRate = rates.get(sourceCurrency.getCurrencyCode());
        BigDecimal destRate = rates.get(destCurrency.getCurrencyCode());
        if(sourceRate == null){
            log.error("No exchange rate found for {}", sourceCurrency);
            return ApiResult.ofError(HttpStatus.BAD_REQUEST, "No exchange rate found for " + sourceCurrency);
        }
        if(destRate == null){
            log.error("No exchange rate found for {}", destCurrency);
            return ApiResult.ofError(HttpStatus.BAD_REQUEST, "No exchange rate found for " + destCurrency);
        }

        //all rates are with respect to USD, thus to get EUR/NOK one needs to use both USD/EUR and USD/NOK
        BigDecimal convertedAmount = amount.divide(sourceRate, MC_5DEC)
                .multiply(destRate, MC_5DEC)
                .round(MC_2DEC);
        log.debug("Conversion {} {} -> {} {}", amount, sourceCurrency, convertedAmount, destCurrency);

        return ApiResult.ofSuccess(convertedAmount);
    }

}
