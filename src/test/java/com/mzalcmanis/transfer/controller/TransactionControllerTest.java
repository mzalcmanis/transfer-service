package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.api.TransactionRequest;
import com.mzalcmanis.transfer.db.repo.TransactionRepository;
import com.mzalcmanis.transfer.dto.Account;
import com.mzalcmanis.transfer.dto.Transaction;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionControllerTest extends RestControllerTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp(){
        super.setUp();
        transactionRepository.deleteAll();
    }

    @Test
    void createTransaction_ok(){
        TransactionRequest txRequest = new TransactionRequest(accClient2Usd.getId(), USD, new BigDecimal(50));
        createTransaction(accUsd.getId(), txRequest)
                .then().statusCode(HttpStatus.OK.value());

        //sender transaction and account
        List<Transaction> transactions = getTransactions(accUsd.getId());
        assertThat(transactions).hasSize(1);
        Transaction txSender = transactions.get(0);
        //sender amount sees negative amount
        assertThat(txSender.getAmount()).isEqualByComparingTo(txRequest.getAmount().negate());
        assertThat(txSender.getCurrency()).isEqualTo(txRequest.getCurrency());

        Account senderAccount = getAccount(client.getId(), accUsd.getId());
        assertThat(senderAccount.getBalance())
                .isEqualByComparingTo(accUsd.getBalance().subtract(txRequest.getAmount()));

        //receiver transaction and account
        List<Transaction> transactionsReceiver = getTransactions(accClient2Usd.getId());
        assertThat(transactionsReceiver).hasSize(1);
        Transaction txReceiver = transactionsReceiver.get(0);
        //receiver sees positive amount
        assertThat(txReceiver.getAmount()).isEqualByComparingTo(txRequest.getAmount());
        assertThat(txReceiver.getId()).isEqualByComparingTo(txReceiver.getId());

        Account receiverAccount = getAccount(client2.getId(), accClient2Usd.getId());
        assertThat(receiverAccount.getBalance())
                .isEqualByComparingTo(accClient2Usd.getBalance().add(txRequest.getAmount()));

    }

    @Test
    void createTransaction_withCurrencyConversion_ok(){
        TransactionRequest txRequest = new TransactionRequest(accClient2Nok.getId(), NOK, new BigDecimal(20));
        createTransaction(accEur.getId(), txRequest)
                .then().statusCode(HttpStatus.OK.value());

        //sender transaction and account
        List<Transaction> transactions = getTransactions(accEur.getId());
        assertThat(transactions).hasSize(1);
        Transaction txSender = transactions.get(0);
        //sender amount sees negative amount
        assertThat(txSender.getAmount()).isEqualByComparingTo(txRequest.getAmount().negate());
        assertThat(txSender.getCurrency()).isEqualTo(txRequest.getCurrency());

    }

    @Test
    void createTransaction_insufficientFunds(){
        createTransaction(accUsd.getId(), new TransactionRequest(accClient2Usd.getId(), USD, accClient2Usd.getBalance().add(new BigDecimal("0.01"))))
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("detail", Matchers.containsStringIgnoringCase("insufficient"));
    }

    @Test
    void createTransaction_accountNotFound(){
        createTransaction(UUID.randomUUID(), new TransactionRequest(accClient2Usd.getId(), USD, new BigDecimal(50)))
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("detail", Matchers.containsStringIgnoringCase("account not found"));


        createTransaction(accUsd.getId(), new TransactionRequest(UUID.randomUUID(), USD, new BigDecimal(50)))
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("detail", Matchers.containsStringIgnoringCase("account not found"));
    }

    @Test
    void createTransaction_wrongCurrency(){
        createTransaction(accUsd.getId(), new TransactionRequest(accClient2Usd.getId(), EUR, BigDecimal.ONE))
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("detail", Matchers.containsStringIgnoringCase("currency"));
    }

    @Test
    void getTransactions_paging(){
        int pageSize = 5;
        //create 2 pages of transactions and not spend all the account's balance
        for (int i = 0; i < pageSize * 2; i++) {
            createTransaction(accUsd.getId(), new TransactionRequest(accClient2Usd.getId(), USD, new BigDecimal(i + 1)))
                    .then().statusCode(HttpStatus.OK.value());
        }
        List<Transaction> transactions = getTransactions(accUsd.getId(), pageSize, 0);
        for (int i = 0; i < pageSize; i++) {
            //amounts decreasinig because of time-based ordering
            BigDecimal amount = new BigDecimal(pageSize * 2 - i).negate();
            assertThat(transactions.get(i).getAmount()).isEqualByComparingTo(amount);
        }

        List<Transaction> transactionsPage2 = getTransactions(accUsd.getId(), pageSize, 1);
        for (int i = 0; i < pageSize; i++) {
            //amounts decreasing because of time-based ordering
            BigDecimal amount = new BigDecimal(pageSize - i).negate();
            assertThat(transactionsPage2.get(i).getAmount()).isEqualByComparingTo(amount);
        }

    }


    private Response createTransaction(UUID senderAccountId, TransactionRequest txRequest) {
        return given().contentType(ContentType.JSON)
                .body(txRequest)
                .when().post("/client/me/accounts/{accountId}/transactions", senderAccountId);
    }

    private List<Transaction> getTransactions(UUID accountId) {
        return given().contentType(ContentType.JSON)
                .when().get("/client/me/accounts/{accountId}/transactions", accountId)
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(new TypeRef<List<Transaction>>() {
                });
    }

    private List<Transaction> getTransactions(UUID accountId, int pageSize, int page) {
        return given().contentType(ContentType.JSON)
                .when().get("/client/me/accounts/{accountId}/transactions?page={page}&size={size}", accountId, page, pageSize)
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(new TypeRef<List<Transaction>>() {
                });
    }

    private Account getAccount(UUID clientId, UUID accountId){
        List<Account> accounts = given().contentType(ContentType.JSON)
                .when().get("/clients/{clientId}/accounts", clientId)
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(new TypeRef<List<Account>>() {
                });
        return accounts.stream()
                .filter(o -> o.getId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Account " + accountId + " not found for client " + clientId));
    }
}
