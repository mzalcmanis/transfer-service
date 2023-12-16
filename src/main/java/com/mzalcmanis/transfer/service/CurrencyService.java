package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.api.ApiResult;

import java.math.BigDecimal;
import java.util.Currency;

public interface CurrencyService {
    ApiResult<BigDecimal> convert(BigDecimal amount, Currency sourceCurrency, Currency destCurrency);
}
