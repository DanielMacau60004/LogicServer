package com.logic.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.others.Utils;
import com.logic.server.data.ProofProblemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
class NDProofsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPLProblems() throws Exception {
        mockMvc.perform(get("/nd/pl/problem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.empty").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLProblems() throws Exception {
        mockMvc.perform(get("/nd/fol/problem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.result.empty").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testPLGetProblemCorrect() throws Exception {
        mockMvc.perform(get("/nd/pl/problem/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testPLGetProblemWrong() throws Exception {
        mockMvc.perform(get("/nd/pl/problem/" + Integer.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLGetProblemCorrect() throws Exception {
        mockMvc.perform(get("/nd/pl/problem/36"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLGetProblemWrong() throws Exception {
        mockMvc.perform(get("/nd/fol/problem/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testPLWithAlgorithm() throws Exception {
        testWithAlgorithm("/nd/pl");
    }

    @Test
    void testFOLWithAlgorithm() throws Exception {
        testWithAlgorithm("/nd/fol");
    }

    private void testWithAlgorithm(String basePath) throws Exception {
        String result = mockMvc.perform(get(basePath + "/problem")
                        .param("size", "100"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
        Map<String, Object> results = (Map<String, Object>) resultMap.get("result");
        List<ProofProblemDTO> proofProblems = objectMapper.convertValue(results.get("content"),
                new TypeReference<>() {});

        for (ProofProblemDTO problem : proofProblems) {
            String[] problemArray = Stream.concat(
                    problem.getPremises().stream(),
                    Stream.of(problem.getConclusion())
            ).toArray(String[]::new);

            result = mockMvc.perform(get(basePath + "/problem/solve")
                            .param("problem", problemArray))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("OK"))
                    .andExpect(jsonPath("$.code").value(200))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            resultMap = objectMapper.readValue(result, Map.class);
            String proof = objectMapper.writeValueAsString(((Map<?, ?>) resultMap.get("result")).get("proof"));

            mockMvc.perform(post(basePath + "/problem")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(proof)
                            .queryParam("problem", problemArray)
                            .queryParam("level", "NONE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            System.out.println(Utils.getToken(
                    "Proving: " + String.join(",", problem.getPremises()) +
                            " |- " + problem.getConclusion()));
        }
    }


}

