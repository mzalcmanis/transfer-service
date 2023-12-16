package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.db.entity.AccountEntity;
import com.mzalcmanis.transfer.db.entity.TransactionEntity;
import com.mzalcmanis.transfer.db.repo.AccountRepository;
import com.mzalcmanis.transfer.db.repo.ClientRepository;
import com.mzalcmanis.transfer.db.repo.TransactionRepository;
import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.dto.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public List<Account> getClientAccounts(UUID clientId) {
        return accountRepository.findByClientId(clientId)
                .stream()
                .map(dtoMapper::map)
                .collect(Collectors.toList());
    }

    public boolean clientExists(UUID clientId) {
        return clientRepository.existsById(clientId);
    }

    public boolean accountExists(UUID accountId) {
        return accountRepository.existsById(accountId);
    }
}
