package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.model.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAccountService {
    Account create(Account account);

    List<Account> getAccounts();

    Account getAccount(Long id);

    List<Account> getAllAccountByOwnerId(Long ownerId);

    Account updateAccount(Long id, Account account);

    Account addBalance(Long id, int amount, Long ownerId);

    Account withdrawBalance(Long id, int amount) throws Exception;

    void delete(Long id);

    void deleteAccountsUsingOwnerId(Long ownerId);

    Account getOneAccountById(Long aId);

    Account createNewOwnerAccount(Long ownerId,Account newAccount);

    Account updateOwnerAccount(Long aid, Long ownerId);

    void deleteOwnerAccount (Long ownerId);

    Account addBalance (Long aId,Integer balance);

    void deleteAllOwnerAccounts(Long ownerId);

    boolean canOwnerLoan (Long OwnerId, Integer RequestPrestamo);

}
