package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Sql(value = "classpath:data_testing.sql")
public class AccountControllerTemplate {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void givenUrl_whenGetAccounts_thenACustomerExist() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Account> entity = new HttpEntity<>(headers);
        ResponseEntity<Account[]> response = restTemplate.exchange("http://localhost:" + port + "/account/all/1", HttpMethod.GET, entity, Account[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    public void givenUrl_whenGetOneAccount_thenACustomerExist() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Account> entity = new HttpEntity<>(headers);
        ResponseEntity<Account> response = restTemplate.exchange("http://localhost:" + port + "/account/1", HttpMethod.GET, entity, Account.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void givenAAcount_whenPostAccountIsNotNullAndWithHeader_thenSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("ACCEPT","application/json");
        Account account = new Account(null,"Company", LocalDate.now(), 100, 1L);
        HttpEntity entity = new HttpEntity<>(account, headers);
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity("http://localhost:"+port+"/account", entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).extracting(Account::getId).isNotEqualTo(0);
    }

    @Test
    public void givenAAcount_whenPostAccountIsNullAndWithHeader_thenDoesntSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("ACCEPT","application/json");
        Account account = null;
        HttpEntity entity = new HttpEntity<>(account, headers);
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity("http://localhost:"+port+"/account", entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).extracting(Account::getId).isNotEqualTo(0);
    }

    @Test
    public void givenAAcount_whenPutAccountIsNotNullAndWithHeader_thenSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("ACCEPT","application/json");
        Account account = new Account(null,"Company", LocalDate.now(), 100, 1L); //Personal -> Company
        HttpEntity entity = new HttpEntity<>(account, headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange("http://localhost:" + port + "/account/1/owner/1", HttpMethod.PUT, entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(responseEntity.getBody()).extracting(Account::getType).isEqualTo("Company");
    }

    @Test
    public void givenAAcount_whenPutAccountIsNullAndWithHeader_thenSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("ACCEPT","application/json");
        Account account = null;
        HttpEntity entity = new HttpEntity<>(account, headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange("http://localhost:" + port + "/account/1/owner/1", HttpMethod.PUT, entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenAAcount_whenDeleteAccountIsNotNullAndWithHeader_thenSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Account> entity = new HttpEntity<>(headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange("http://localhost:" + port + "/account/1", HttpMethod.DELETE, entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void givenAAcount_whenDeleteAccountIsNullAndWithHeader_thenSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Account> entity = new HttpEntity<>(headers);
        ResponseEntity<Account> responseEntity = restTemplate.exchange("http://localhost:" + port + "/account/100", HttpMethod.DELETE, entity, Account.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
