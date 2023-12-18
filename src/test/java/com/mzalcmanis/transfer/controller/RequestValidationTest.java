package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.api.TransactionRequest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

//Might be aggregated in one test method if the api would return all validation errors
//instead of the first one
public class RequestValidationTest extends RestControllerTest {

    @Test
    void negativeAmount_error() {
        createInvalidTransaction(
                new TransactionRequest(accClient2Usd.getId(), USD.getCurrencyCode(), BigDecimal.ONE.negate()),
                "amount"
        );
    }

    @Test
    void nullAccount_error() {
        createInvalidTransaction(
                new TransactionRequest(null, USD.getCurrencyCode(), BigDecimal.ONE),
                "receiverAccountId"
        );
    }

    @Test
    void invalidCurrency_error() {
        createInvalidTransaction(
                new TransactionRequest(accClient2Usd.getId(), "BLA", BigDecimal.ONE),
                "currency"
        );
    }

    private void createInvalidTransaction(TransactionRequest txRequest, String invalidFieldName) {
        given().contentType(ContentType.JSON)
                .body(txRequest)
                .when().post("/client/me/accounts/{accountId}/transactions", accUsd.getId())
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("detail", Matchers.containsStringIgnoringCase(invalidFieldName));
    }

}
