package com.mzalcmanis.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private UUID id;

    private UUID clientId;

    private String accountNumber;

    private Currency currency;

    private BigDecimal balance;

}
