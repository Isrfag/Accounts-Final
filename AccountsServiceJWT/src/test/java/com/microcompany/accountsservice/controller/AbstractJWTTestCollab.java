package com.microcompany.accountsservice.controller;

import com.jayway.jsonpath.JsonPath;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public abstract class AbstractJWTTestCollab {

    @Autowired
    protected MockMvc mockMvc;

    protected String obtainAccessToken(String username, String password) throws Exception {

        User usuario = new User(username,password);

        MvcResult response = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.asJsonString(usuario))
        ).andExpect(status().isOk())
        .andReturn();

       String tokenResponse = response.getResponse().getContentAsString();

       String token = JsonPath.read(tokenResponse,"$.accessToken");

       return token;
    }

    protected int tryToken(String token) throws Exception {

        HttpHeaders headers = new HttpHeaders();

        MvcResult response = mockMvc.perform(get("/account/all/1")
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
        .andReturn();

        return response.getResponse().getStatus();
    }

}
