package com.logic.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.api.IFOLFormula;
import com.logic.api.INDProof;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.nd.algorithm.AlgoProofFOLBuilder;
import com.logic.nd.algorithm.AlgoProofPLBuilder;
import com.logic.nd.algorithm.AlgoSettingsBuilder;
import com.logic.nd.algorithm.state.strategies.SizeTrimStrategy;
import com.logic.server.data.ProofProblemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        mockMvc.perform(get("/nd/pl/problem/36"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLGetProblemCorrect() throws Exception {
        mockMvc.perform(get("/nd/fol/problem/36"))
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
    void testPLTestProblemCorrect() throws Exception {
        String problem = "[∨E, 5, 6] [a. [→E] [a ∨ b. [⊥, 7] [p. [∨E, 8, 9] [⊥. [H, 3] [r ∨ ¬q.] [¬E] [⊥. [∧ER] [¬r. [H, 4] [¬r ∧ ¬b.]] [H, 8] [r.]] [¬E] [⊥. [H, 9] [¬q.] [→E] [q. [H, 7] [¬p.] [H, 2] [¬p → q.]]]]] [H, 1] [p → (a ∨ b).]] [H, 5] [a.] [⊥, 10] [a. [¬E] [⊥. [∧EL] [¬b. [H, 4] [¬r ∧ ¬b.]] [H, 6] [b.]]]]";

        mockMvc.perform(post("/nd/pl/problem/32").content(problem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testPLTestProblemWrong() throws Exception {
        String problem = "[∨E, 5, 6] [a. [→E] [a ∨ b. [⊥, 7] [p. [∨E, 8, 9] [⊥. [H, 3] [r ∨ ¬q.] [¬E] [⊥. [∧ER] [¬r. [H, 4] [¬r ∧ ¬b.]] [H, 8] [r.]] [¬E] [⊥. [H, 9] [¬q.] [→E] [q. [H, 7] [¬p.] [H, 2] [¬p → q.]]]]] [H, 1] [p → (a ∨ b).]] [H, 5] [a.] [⊥, 10] [a. [¬E] [⊥. [∧EL] [¬b. [H, 4] [¬r ∧ ¬b.]] [H, 6] [b.]]]]";

        mockMvc.perform(post("/nd/pl/problem/31").content(problem))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLTestProblemCorrect() throws Exception {
        String problem = "[∃I] [∃x L(x,a). [→E] [L(a,a). [∃I] [∃y (L(y,a) ∨ L(a,y)). [∨IL] [L(b,a) ∨ L(a,b). [H, 2] [L(a,b).]]] [∀E] [∃y (L(y,a) ∨ L(a,y)) → L(a,a). [H, 1] [∀x (∃y (L(y,x) ∨ L(x,y)) → L(x,x)).]]]]";

        mockMvc.perform(post("/nd/fol/problem/40").content(problem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testFOLTestProblemWrong() throws Exception {
        String problem = "[∃I] [∃x L(x,a). [→E] [L(a,a). [∃I] [∃y (L(y,a) ∨ L(a,y)). [∨IL] [L(b,a) ∨ L(a,b). [H, 2] [L(a,b).]]] [∀E] [∃y (L(y,a) ∨ L(a,y)) → L(a,a). [H, 1] [∀x (∃y (L(y,x) ∨ L(x,y)) → L(x,x)).]]]]";

        mockMvc.perform(post("/nd/fol/problem/36").content(problem))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    @Test
    void testPLWithAlgorithm() throws Exception {
        String result = mockMvc.perform(get("/nd/pl/problem"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
        Map<String, Object> results = (Map<String, Object>) resultMap.get("result");
        List<ProofProblemDTO> proofProblems = objectMapper.convertValue(results.get("content"),
                new TypeReference<>() {
                });

        for (ProofProblemDTO problem : proofProblems) {
            IPLFormula conclusion = LogicAPI.parsePL(problem.getConclusion());
            Set<IPLFormula> premises = new HashSet<>();

            for (String premise : problem.getPremises())
                premises.add(LogicAPI.parsePL(premise));

            INDProof proof = new AlgoProofPLBuilder(conclusion)
                    .addPremises(premises)
                    .build();

            mockMvc.perform(post("/nd/pl/problem/" + problem.getId()).content(proof.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }
    }

    @Test
    void testFOLWithAlgorithm() throws Exception {
        String result = mockMvc.perform(get("/nd/fol/problem"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
        Map<String, Object> results = (Map<String, Object>) resultMap.get("result");
        List<ProofProblemDTO> proofProblems = objectMapper.convertValue(results.get("content"),
                new TypeReference<>() {
                });

        for (ProofProblemDTO problem : proofProblems) {
            IFOLFormula conclusion = LogicAPI.parseFOL(problem.getConclusion());
            Set<IFOLFormula> premises = new HashSet<>();

            for (String premise : problem.getPremises())
                premises.add(LogicAPI.parseFOL(premise));

            INDProof proof = new AlgoProofFOLBuilder(conclusion)
                    .addPremises(premises)
                    .setAlgoSettingsBuilder(
                            new AlgoSettingsBuilder()
                                    .setTotalClosedNodes(10000)
                                    .setHypothesesPerState(3)
                                    .setTimeout(500)
                                    .setTrimStrategy(new SizeTrimStrategy()))
                    .addTerm(new ASTVariable("w"))
                    .build();

            mockMvc.perform(post("/nd/fol/problem/" + problem.getId()).content(proof.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                    .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }
    }

}

