package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.api.ApiResult;
import com.mzalcmanis.transfer.db.entity.AccountEntity;
import com.mzalcmanis.transfer.db.entity.TransactionEntity;
import com.mzalcmanis.transfer.db.repo.AccountRepository;
import com.mzalcmanis.transfer.db.repo.ClientRepository;
import com.mzalcmanis.transfer.db.repo.TransactionRepository;
import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.dto.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: interface
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    private final DtoMapper dtoMapper;

    public ApiResult<List<Account>> getClientAccounts(UUID clientId) {
        if(!clientRepository.existsById(clientId)){
            return ApiResult.ofError(HttpStatus.NOT_FOUND, "client not found");
        }
        List<Account> accountList = accountRepository.findByClientId(clientId)
                .stream()
                .map(dtoMapper::map)
                .collect(Collectors.toList());
        return ApiResult.ofSuccess(accountList);
    }

}
