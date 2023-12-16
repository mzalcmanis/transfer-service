package com.mzalcmanis.transfer.service.impl;

import com.mzalcmanis.transfer.api.ApiResult;
import com.mzalcmanis.transfer.db.repo.AccountRepository;
import com.mzalcmanis.transfer.db.repo.ClientRepository;
import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    private final DtoMapper dtoMapper;

    @Override
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
