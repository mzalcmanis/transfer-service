package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.api.TransactionRequest;
import com.mzalcmanis.transfer.dto.Transaction;
import com.mzalcmanis.transfer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/client/me/accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable("accountId") UUID accountId,
            Pageable pageable
    ){
        return transactionService.getTransactions(accountId, pageable).toResponseEntity();
    }

    @PostMapping("/client/me/accounts/{accountId}/transactions")
    public ResponseEntity<Void> createTransaction(
            @PathVariable("accountId") UUID senderAccountId,
            @RequestBody TransactionRequest request
    ){
        return transactionService.createTransaction(request, senderAccountId).toResponseEntity();
    }
}
