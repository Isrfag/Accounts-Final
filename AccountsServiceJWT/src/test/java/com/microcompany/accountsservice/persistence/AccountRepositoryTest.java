package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.model.Account;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest()
@ActiveProfiles("dev")
@Sql(value = "classpath:data_testing.sql")
class AccountRepositoryTest {

    @Autowired
    AccountRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    @Order(1)
    void givenAnAccount_WhenCreateAccounts_ThenAccountIsValid(){
        Account account = new Account(null, "Personal", LocalDate.now(), 100, 1L);
        assertThat(repository.save(account)).isNotNull();
    }

    @Test
    void givenAnInvalidadAccount_WhenAccountIsNotValid_ThenReturnException(){
        Account account = null;
        assertThrows(Exception.class,() ->
            repository.save(account)
        );
    }

    @Test
    void givenAnAccount_WhenAidIsNotNull_ThenReturnAccount(){
        assertThat(repository.findById(1L).get()).isNotNull();
    }

    @Test
    void givenAnInvalidAccount_WhenAidIsNull_ThenReturnException(){
        assertThrows(Exception.class,() ->
                repository.findById(100L).get()
        );
    }

    @Test
    void givenAccount_WhenOwnerIdIsGood_ThenReturnAccount(){
        List<Account> accounts = repository.findByOwnerId(1L);
        assertThat(accounts).isNotNull().isNotEmpty();
    }

    @Test
    void givenAccounts_WhenOwnerIdIsInvalid_ThenReturnNull(){
        List<Account> accounts = null;
        assertThat(accounts).isNull();
    }

    @Test
    void givenAccountsByOwnerId_WhenOwnerIdIsInvalidAndHaveNoAccounts_ThenReturnAnEmptyList(){
        List<Account> accounts = repository.findByOwnerId(5L);
        assertThat(accounts).isEmpty();
    }

    @Test
    void givenAnAccountToUpdate_WhenOwnerIdIsValid_ThenReturnUpdatedAccount(){
        Account account = repository.findById(1L).get();
        account.setType("Company");
        assertThat(repository.save(account)).isNotNull();
    }

    @Test
    void givenAnAccountToUpdate_WhenAccountCannotBeUpadted_ThenReturnNull(){
        Account account = null;
        assertThrows(Exception.class,() ->
            repository.save(account)
        );
    }

    @Test
    void givenAnAccountToUpdateByOwnerId_WhenAccountToUpdateDoesntExist_ThenReturnException(){
        assertThrows(Exception.class,() ->
           repository.findById(40L).get()
        );
    }

    @Test
    void givenAnAccountUpdateAccountByOwnerId_WhenCustomerIsNull_ThenReturnException(){
        Long customerId = null;
        assertThrows(Exception.class,() ->
            customerRepository.findById(customerId)
        );
    }

    @Test
    void givenAnAccountToRemoveByOwnerId_WhenAccountIsNotNull_ThenReturnFalse(){
        assertTrue(repository.findById(1L).isPresent());
        repository.deleteById(1L);
        assertFalse(repository.findById(1L).isPresent());
    }

    @Test
    void givenAnAccountToRemoveOwnerId_WhenAccountIsNotNull_ThenReturnLessThan1(){
        assertThat(repository.findByOwnerId(1L).size()).isGreaterThan(0);
        repository.deleteByOwnerId(1L);
        assertThat(repository.findByOwnerId(1L).size()).isLessThan(1);
    }

    @Test
    void givenAnAccountToRemoveByOwnerId_WhenAccountIsNull_ThenReturnException(){
        Long account = null;
        assertThrows(Exception.class,() ->
            repository.findById(account)
        );
    }

    @Test
    void givenAnAccountToRemoveAccountsByOwnerId_WhenCustomerIsNull_ThenReturnException(){
        assertThrows(Exception.class,() ->
           customerRepository.findById(40L).get()
        );
    }

}