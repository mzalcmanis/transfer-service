package com.mzalcmanis.transfer.api;


import com.mzalcmanis.transfer.controller.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull
    private UUID receiverAccountId;

    @NotNull
    @ValidCurrency
    private String currency;

    @Positive
    private BigDecimal amount;

}
