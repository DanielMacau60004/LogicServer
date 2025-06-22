package com.logic.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ExpControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPLExpressionCorrect() throws Exception {
        mockMvc.perform(post("/exp/pl").content("(a ∧ b) ∨ (a ∧ c)"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testPLExpressionWrong() throws Exception {
        mockMvc.perform(post("/exp/pl").content("(a ∧ b) ∨ a ∧ c"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLExpressionCorrect() throws Exception {
        mockMvc.perform(post("/exp/fol").content("∀x(∃y(L(y,x) ∨ L(x,y)) → L(x,x))"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLExpressionWrong() throws Exception {
        mockMvc.perform(post("/exp/fol").content("∀x(∃y(L(y,x,z) ∨ L(x,y)) → L(x,x))"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

