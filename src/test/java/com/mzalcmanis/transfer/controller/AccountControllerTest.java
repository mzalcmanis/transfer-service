package com.mzalcmanis.transfer.controller;

import com.mzalcmanis.transfer.dto.Account;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountControllerTest extends RestControllerTest {

    @Test
    void getAccounts_allAccounts_found() {
        List<Account> accounts = given().contentType(ContentType.JSON)
                .when().get("/clients/{clientId}/accounts", client.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(new TypeRef<List<Account>>() {
                });
        assertEquals(accounts.size(), 3);
        assertThat(accounts.stream().map(Account::getId).collect(Collectors.toList()))
                .containsExactlyInAnyOrder(accUsd.getId(), accEur.getId(), accNok.getId());

    }

    @Test
    void getAccounts_noAccountsYet_EmptyList() {
        List<Account> accounts = given().contentType(ContentType.JSON)
                .when().get("/clients/{clientId}/accounts", clientNoAccounts.getId())
                .then().statusCode(HttpStatus.OK.value())
                .extract().as(new TypeRef<List<Account>>() {
                });
        assertThat(accounts).isEmpty();

    }

    @Test
    void getAccounts_clientDoesNotExist_notFoundError() {
        given().contentType(ContentType.JSON)
                .when().get("/clients/{clientId}/accounts", UUID.randomUUID())
                .then().statusCode(HttpStatus.NOT_FOUND.value());

    }
}