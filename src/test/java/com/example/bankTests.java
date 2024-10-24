package com.example;

import com.example.assignement.AssignementApplication;
import com.example.assignement.controllers.BankController;
import com.example.assignement.domains.Customer;
import com.example.assignement.repositories.CustomerRepository;
import com.example.assignement.services.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AssignementApplication.class)
@AutoConfigureMockMvc

public class bankTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private BankService bankService;
//
//    @Test
//    void testCreateAccount() throws Exception {
//
//        mockMvc.perform(post("/accounts")
//
//                .param("customerId", "1").param("initialDeposit", "1000")).andExpect(status().isOk());
//    }

}
