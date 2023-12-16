package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.db.entity.AccountEntity;
import com.mzalcmanis.transfer.db.entity.TransactionEntity;
import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.dto.Transaction;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class DtoMapper {

    Account map(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .balance(entity.getBalance())
                .currency(entity.getCurrency())
                .clientId(entity.getClientId())
                .accountNumber(entity.getAccountNumber())
                .build();
    }

    Transaction map(TransactionEntity entity, UUID accountId){
        return Transaction.builder()
                .id(entity.getId())
                .currency(entity.getCurrency())
                .createdDate(entity.getCreatedDate())
                .amount(accountId.equals(entity.getSenderAccountId())
                        ? entity.getAmount().negate()
                        : entity.getAmount()
                )
                .build();
    }
}
