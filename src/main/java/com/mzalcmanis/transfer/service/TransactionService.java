package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.api.ApiResult;
import com.mzalcmanis.transfer.api.TransactionRequest;
import com.mzalcmanis.transfer.dto.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    ApiResult<Void> createTransaction(TransactionRequest request, UUID senderAccountId);

    ApiResult<List<Transaction>> getTransactions(UUID accountId, Pageable pageable);
}
