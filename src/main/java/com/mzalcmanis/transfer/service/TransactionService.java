package com.mzalcmanis.transfer.service;

import com.mzalcmanis.transfer.api.TransactionRequest;
import com.mzalcmanis.transfer.db.entity.AccountEntity;
import com.mzalcmanis.transfer.db.entity.TransactionEntity;
import com.mzalcmanis.transfer.db.repo.AccountRepository;
import com.mzalcmanis.transfer.db.repo.TransactionRepository;
import com.mzalcmanis.transfer.dto.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: interface
@Service
@RequiredArgsConstructor
public class TransactionService {

    //TODO: remove unused
    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final DtoMapper dtoMapper;

    private final CurrencyService currencyService;

    //Use ProblemDetail just to show the idea of error handling
    //Normally we would have some error dto class with enumerated errors and error message localizations
    @Transactional
    public Optional<ProblemDetail> createTransaction(TransactionRequest request, UUID senderAccountId){
        var senderAccountOptional = accountRepository.findById(senderAccountId);
        if(senderAccountOptional.isEmpty()){
            return Optional.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Sender account not found"));
        }
        var receiverAccountOptional = accountRepository.findById(request.getReceiverAccountId());
        if(receiverAccountOptional.isEmpty()){
            return Optional.of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Receiver account not found"));
        }
        AccountEntity senderAcc = senderAccountOptional.get();
        AccountEntity receiverAcc = receiverAccountOptional.get();
        if(!receiverAcc.getCurrency().equals(request.getCurrency())){
            return Optional.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    String.format("Receiver account currency %s does not match the transaction currency %s",
                            receiverAcc.getCurrency(), request.getCurrency()))
            );
        }
        Optional<BigDecimal> convertedAmountOptional = currencyService.convert(request.getAmount(), request.getCurrency(), senderAcc.getCurrency());
        if(convertedAmountOptional.isEmpty()){
            return Optional.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    String.format("Currency conversion %s -> %s failed ",
                            receiverAcc.getCurrency(), request.getCurrency()))
            );
        }
        BigDecimal convertedAmount = convertedAmountOptional.get();
        if(senderAcc.getBalance().compareTo(convertedAmount) < 0){
            return Optional.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient funds")
            );
        }
        //TODO: split here due to transaction?
        senderAcc.setBalance(senderAcc.getBalance().subtract(convertedAmount));
        //transaction and receiver accounts currency is always from the request by business requirement
        receiverAcc.setBalance(receiverAcc.getBalance().add(request.getAmount()));
        TransactionEntity transaction = TransactionEntity.builder()
                .senderAccountId(senderAcc.getId())
                .receiverAccountId(receiverAcc.getId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .createdDate(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);
        return Optional.empty();
    }

    public List<Transaction> getTransactions(UUID accountId, Pageable pageable) {
        return transactionRepository.findByAccountId(accountId, pageable)
                .stream()
                .map(transactionEntity -> dtoMapper.map(transactionEntity, accountId))
                .collect(Collectors.toList());
    }

    //TODO: do transaction

}
