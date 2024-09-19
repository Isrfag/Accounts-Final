package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.exception.AccountNotFoundException;
import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.model.Customer;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.persistence.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
class IAccountServiceTest {

    @TestConfiguration
    static class AccountServiceConfiguration {
        @Bean
        public AccountService accountService () {
            return new AccountService();
        }
    }
    @BeforeEach
    public void SetUp () {
        Customer acustomer = new Customer(1l, "Reimon", "RemonGmail.com");
        List<Account> accounts = List.of(new Account(1L, "Company", LocalDate.now(), 1000, 1l));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(acustomer));
        when(accountRepo.findByOwnerId(1L)).thenReturn(accounts);

        when(customerRepository.findById(10L)).thenReturn(Optional.of(acustomer));
        when(accountRepo.findByOwnerId(10L)).thenThrow(new AccountNotFoundException(10l));

        when(accountRepo.save(Mockito.any(Account.class)))
                .thenAnswer(element -> {
                    Account ap = (Account) element.getArguments()[0];
                    ap.setId(100L);
                    return ap;
                });

    }
    @Autowired
    AccountService service;
    @MockBean
    AccountRepository accountRepo;

    @MockBean
    CustomerRepository customerRepository;

    @Test
    void givenAServiceBean_WhenTesting_ThenIsNotNull() {
        assertNotNull(service);
    }

    @Test
    void given_ACustomerId_WhenCustomerIdIsValid_ThenCreateNewAccountForCustomer() {

        assertThat(service).isNotNull();
        Account theAccount= new Account(1l,"Company",LocalDate.now(),1000,1l);
        Account newAccount= service.createNewOwnerAccount(1l,theAccount);

        assertThat(newAccount).extracting(Account::getOwnerId).isEqualTo(theAccount.getOwnerId());

    }

    @Test
    void givenACustomerId_WhenCustomerIdIsValid_ThenReturnAccounts() {
        assertThat(service).isNotNull();
        List<Account> accounts = service.getAllAccountByOwnerId(1l);
        assertThat(accounts).isNotNull().isNotEmpty();

    }

    @Test
    void givenACustomerId_WhenCustomerIdNotExist_ThenReturnException() {
        assertThat(service).isNotNull();
        assertThrows(AccountNotFoundException.class, () -> service.getAllAccountByOwnerId(10l));
    }

    @Test
    void givenACustomerId_WhenCustomerAccountExist_ThenDeleleteAndReturnTrue() {
        assertThat(service).isNotNull();
        List<Account> accounts = service.getAllAccountByOwnerId(1l);
        service.deleteAccountsUsingOwnerId(1L);
        Mockito.verify(accountRepo,Mockito.times(1)).deleteByOwnerId(1l);
    }

    @Test
    void givenACustomerId_WhenCustomerAccountNotExist_ThenReturnException() {
        assertThat(service).isNotNull();
        when(accountRepo.existsById(10l)).thenReturn(false);
        assertThrows(AccountNotFoundException.class, () -> service.deleteOwnerAccount(10L));
    }
}
