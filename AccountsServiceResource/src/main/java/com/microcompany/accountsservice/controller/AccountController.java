package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    @Autowired
    IAccountService accountService;

    @RequestMapping(value = "",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createAccount (@RequestBody Account newAccount) {
        //revisar exception

        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.create(newAccount));

    }

    @RequestMapping(value="/{cid}", method= RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Account> createAccountByOwnerId(@PathVariable("cid") Long ownerId,@RequestBody Account newAccount) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.createNewOwnerAccount(ownerId,newAccount));
    }

    @RequestMapping(value = "/all/{cid}",method = RequestMethod.GET)
    public ResponseEntity <List<Account>> getAccountsByCustomer(@PathVariable("cid") Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAllAccountByOwnerId(id));

    }

    @RequestMapping(value="/{aid}",method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity getSingleAccount(@PathVariable("aid") Long id) {
        //RE
        Account account = accountService.getOneAccountById(id);
        return new ResponseEntity(account,HttpStatus.OK);
    }

    @RequestMapping(value="/{aid}/owner/{cid}",method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity updateAccountByOwnerId (@PathVariable ("aid") Long aid,@PathVariable("cid") Long ownerId,@RequestBody Account account ) {

        account.setId(aid);
        account.setOwnerId(ownerId);
        accountService.updateOwnerAccount(aid,ownerId);
        return new ResponseEntity(account, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/{aid}",method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity deleteAccount(@PathVariable("aid") Long id) {
        //Devolver cuerpo y RE
        accountService.delete(id);
        return new ResponseEntity(id,HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/owner/{cid}",method = RequestMethod.DELETE, consumes = "application/json")
    public ResponseEntity deleteOwnerAccount(@PathVariable("cid")Long ownerId ) {
        accountService.deleteOwnerAccount(ownerId);
        return new ResponseEntity(ownerId,HttpStatus.NO_CONTENT);
    }


    @RequestMapping(value ="/ownerall/{cid}",method = RequestMethod.DELETE,consumes = "application/json")
    public ResponseEntity deleteAccountsByOwnerId(@PathVariable("cid") Long cid) {

        //RE
        accountService.deleteAllOwnerAccounts(cid);
        return new ResponseEntity(cid,HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{aid}/withdrawbalance/{monto}",method = RequestMethod.POST,  consumes = "application/json")
    public ResponseEntity addBalanceToAccount(@PathVariable("aid")Long id, @PathVariable("monto") Integer balance) {
        //RE
        return new ResponseEntity(accountService.addBalance(id,balance),HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{aid}/addbalance/{monto}", method = RequestMethod.POST , consumes = "application/json")
    public ResponseEntity withdrawBalanceToAccount(@PathVariable("aid")Long id, @PathVariable("monto") Integer balance) {
        //RE
        return new ResponseEntity(accountService.addBalance(id,balance),HttpStatus.ACCEPTED);
    }

    @GetMapping("/canloan/{cid}/{loan}")
    public ResponseEntity canLoan(@PathVariable("cid") Long id, @PathVariable("loan") Integer loan)  {
        //incluir metodo prestamo y RE
        return null;
    }

}
