package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("dev")
@Sql(value = "classpath:data_testing.sql")
public class AccountControllerAllContextTest {

    @Autowired
    private AccountController accountController;

     @Test
    void givenCustomerId_whenGetAllAccounts_thenIsNotNull() {
        ResponseEntity<List<Account>> response = accountController.getAccountsByCustomer(1l);
        assertThat(response.getStatusCode().value())
                .isEqualTo(200);

        assertThat(response.getBody())
                .isNotNull()
                .isNotEmpty();

    }

    @Test
    void givenOwnerId_whenGetAllWithWrongOwnerId_ThenIsEmpty() {
        ResponseEntity<List<Account>> response = accountController.getAccountsByCustomer(8l);
        assertThat(response.getStatusCode().value())
                .isEqualTo(200);

        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void givenOwnerId_whenValidCreateAccount_thenIsCreatedAndHaveId() {

        Account newAccount = new Account(1L, "Company", LocalDate.now(), 1000, 1l);
        ResponseEntity<Account> response = accountController.createAccountByOwnerId(newAccount.getOwnerId(),newAccount);

        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody().getId()).isGreaterThan(0);
        assertThat(response.getBody()).extracting(Account::getOwnerId).isEqualTo(newAccount.getOwnerId());

    }

    @Test
    void givenAccounts_whenCreateWithInvalidOwnerId_thenReturnException() {
        Account newAccount = new Account(1L, "Company", LocalDate.now(), 1000, 100l);
        //ResponseEntity <Account> response = accountController.createAccountByOwnerId(newAccount.getOwnerId(),newAccount);
        assertThrows(CustomerNotFoundException.class,()-> accountController.createAccountByOwnerId(newAccount.getOwnerId(),newAccount));
        //assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
