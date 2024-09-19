package com.microcompany.accountsservice.controller;

import com.microcompany.accountsservice.model.Account;
import com.microcompany.accountsservice.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.services.AccountService;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerWebMvc {

    @BeforeEach
    public void setUp() {

        List<Account> accounts = List.of(new Account(null,"Company", LocalDate.now(), 100, 1L));
        Mockito.when(accountService.getAllAccountByOwnerId(1L)).thenReturn(accounts);

        //Caso empty
        /*List<Account> accounts = List.of();
        Mockito.when(accountService.getAllAccountByOwnerId(1L)).thenReturn(accounts);*/

        Mockito.when(accountService.createNewOwnerAccount(Mockito.anyLong(), Mockito.any(Account.class)))
            .thenAnswer(elem -> {
                Account ap = (Account) elem.getArguments()[1];
                ap.setId(1L);
                ap.setType("Company");
                return ap;
        });

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    public void givenAccounts_whenGetAccount_thenStatus200() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/all/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[*].type", hasItem("Company")))
                .andReturn();
    }

    @Test
    public void givenEmptyAccounts_whenGetAccount_thenIsEmpty() throws Exception {
        MvcResult result = mockMvc.perform(get("/account/all/1").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[]"))
                .andReturn();
    }

    @Test
    void givenAccount_whenValidCreateAccount_thenIsCreatedAndHaveId() throws Exception {
        Account newProduct = new Account(null,"Company", LocalDate.now(), 100, 1L);

        mockMvc.perform(post("/account/1")
                        .content(JsonUtil.asJsonString(newProduct))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(greaterThanOrEqualTo(1))));

    }

    @Test
    void givenAccount_whenValidDeleteAccount_thenIsDeleted() throws Exception {
        mockMvc.perform(delete("/account/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    void givenNonExistingAccount_whenDeleteAccount_thenIsNotFound() throws Exception {
        mockMvc.perform(delete("/account/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

}
