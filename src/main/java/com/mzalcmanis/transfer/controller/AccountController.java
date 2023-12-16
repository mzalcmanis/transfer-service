package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    //TODO:
    @GetMapping("/clients/{clientId}/accounts")
    public ResponseEntity<List<Account>> getAccounts(
            //we rely on receiving a valid UUID string, to handle malformed params
            // one would use RequestBodyAdvice or OncePerRequestFilter
            @PathVariable("clientId") UUID clientId
    ){
        return accountService.getClientAccounts(clientId).toResponseEntity();
    }

}
