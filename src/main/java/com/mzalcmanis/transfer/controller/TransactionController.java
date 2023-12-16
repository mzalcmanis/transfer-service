package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.api.TransactionRequest;
import com.mzalcmanis.transfer.dto.Transaction;
import com.mzalcmanis.transfer.service.AccountService;
import com.mzalcmanis.transfer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    //TODO: use /client/me/accounts... ?
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable("accountId") UUID accountId, Pageable pageable){
        if(!accountService.accountExists(accountId)){
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Account Not found")).build();
        }
        //TODO: RestController exception handler
        return ResponseEntity.ok(transactionService.getTransactions(accountId, pageable));
    }

    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<Void> createTransaction(@PathVariable("accountId") UUID senderAccountId, @RequestBody TransactionRequest request){
        Optional<ProblemDetail> errorResult = transactionService.createTransaction(request, senderAccountId);
        if(errorResult.isEmpty()){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.of(errorResult.get()).build();
    }
}
