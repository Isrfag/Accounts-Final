package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.ERole;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.persistence.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("dev")
@Sql(value = "classpath:data_testing.sql")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerAllContextTest extends AbstractJWTTestCollab {

    @Autowired
    private AccountController accountController;

    private String emailD = "t@t.com";
    private String emailC ="cajero@aa.es";
    private String password = "tpasswrd";
    private String accessToken = null;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void SetUpUsers() {
        // Create user
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String enc_password = passwordEncoder.encode(password);

        User director = new User(null, emailD, enc_password, ERole.DIRECTOR);
        userRepository.save(director);

        String enc_password2 = passwordEncoder.encode(password);

        User cajero = new User(null, emailC, enc_password2, ERole.CAJERO);
        userRepository.save(cajero);


    }


    @Test
    public void givenADirector_WhenUserEmailandPasswordAreValid_ThenReturnToken() throws Exception {
        String token = this.obtainAccessToken("t@t.com","tpasswrd");
        assertThat(token)
                .isNotNull().isNotEmpty();

    }

    @Test
    public void givenACajero_WhenCajeroIsLogged_ThenHasAccessToAccountController() throws Exception {
        assertThat(
                this.obtainAccessToken("cajero@aa.es","tpasswrd"))
                .isNotNull().isNotEmpty();
    }

    @Test
    public void givenADirector_WhenDirectorIsLogged_ThenTokenIsOnHeaders() throws Exception{
        String token = this.obtainAccessToken("t@t.com","tpasswrd");
        assertThat(this.tryToken(token)).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void givenACajero_WhenCajeroIsLogged_ThenTokenIsWorkingAndOnHeaders() throws Exception {
        String token = this.obtainAccessToken("cajero@aa.es","tpasswrd");
        assertThat(this.tryToken(token)).isEqualTo(HttpStatus.OK.value());
    }



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
