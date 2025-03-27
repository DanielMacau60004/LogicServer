package com.logic.server.server.data;

import com.logic.api.LogicAPI;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SampleProblems implements CommandLineRunner {

    private final ProofProblemRepository userRepository;

    public SampleProblems(ProofProblemRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        List<ProofProblemDAO> users = List.of(
                createNDProofProblem("a → (a ∨ b)", false),
                createNDProofProblem("(a ∨ a) → a", false),
                createNDProofProblem("(a ∧ b) → a", false),
                createNDProofProblem("a → (b → a)", false),
                createNDProofProblem("(a → b); (b → c) → (a → c)", false),
                createNDProofProblem("(a → (b → c)); (a → b) → (a → c)", false),
                createNDProofProblem("(a → b) → (a → (b ∨ c))", false),
                createNDProofProblem("(b → c) → ((a ∧ b) → c)", false),
                createNDProofProblem("¬(a ∨ b) → ¬a", false),
                createNDProofProblem("(b → c) → ((a ∧ b) → (a ∧ c))", false),

                createNDProofProblem("¬a → (a → b)", false),
                createNDProofProblem("((a → b) ∧ ¬b) → ¬a", false),
                createNDProofProblem("(a → ¬¬a); (¬¬a → a)", false),
                createNDProofProblem("((a → b) → (¬b → ¬a)); ((¬b → ¬a) → (a → b))", false),
                createNDProofProblem("⊥ → a", false),
                createNDProofProblem("a ∨ ¬a", false),
                createNDProofProblem("((a → d) → a) → a", false),
                createNDProofProblem("a ∨ (a → b)", false),
                createNDProofProblem("(a → b) ∨ (b → d)", false),

                createNDProofProblem("¬a ∨ b; a → b", false),
                createNDProofProblem("a → b; ¬a ∨ b", false),
                createNDProofProblem("¬(a ∧ b); ¬a ∨ ¬b", false),
                createNDProofProblem("¬a ∨ ¬b; ¬(a ∧ b)", false),
                createNDProofProblem("¬(a ∨ b); ¬a ∧ ¬b", false),
                createNDProofProblem("¬a ∧ ¬b; ¬(a ∨ b)", false),
                createNDProofProblem("a ∨ (b ∧ c); (a ∨ b) ∧ (a ∨ c)", false),
                createNDProofProblem("(a ∨ b) ∧ (a ∨ c); a ∨ (b ∧ c)", false),
                createNDProofProblem("a ∧ (b ∨ c); (a ∧ b) ∨ (a ∧ c)", false),
                createNDProofProblem("(a ∧ b) ∨ (a ∧ c); a ∧ (b ∨ c)", false),
                createNDProofProblem("(a → b) ∧ (b → a); ((a ∧ c) → (b ∧ c)) ∧ ((b ∧ c) → (a ∧ c))", false),
                createNDProofProblem("¬(¬a ∨ ¬b); a ∧ b", false),

                createNDProofProblem("¬p → q; r ∨ ¬q; p → (a ∨ b); ¬r ∧ ¬b; a", false),

                createNDProofProblem("∀x P(x) ∨ ∀x Q(x); ∀x (P(x) ∨ Q(x))", true),
                createNDProofProblem("∀x (P(x) ∧ Q(x)); ∀x P(x) ∧ ∀x Q(x)", true),
                createNDProofProblem("(∀x P(x) ∧ ∀x Q(x)) → ∀x (P(x) ∧ Q(x))", true),
                createNDProofProblem("∃x (P(x) ∧ Q(x)); ∃x P(x) ∧ ∃x Q(x)", true),
                createNDProofProblem("(∃x P(x) ∨ ∃x Q(x)) → ∃x (P(x) ∨ Q(x))", true),
                createNDProofProblem("∀y (C(y) ∨ D(y)); ∀x (C(x) → L(x)); ∃x ¬L(x); ∃x D(x)", true),
                createNDProofProblem("∀x (C(x) → S(x)); ∀x (¬A(x,b) → ¬S(x)); ∀x ((C(x)∨S(x)) → A(x,b))", true),
                createNDProofProblem("L(a,b); ∀x (∃y (L(y,x) ∨ L(x,y)) → L(x,x)); ∃x L(x,a)", true)
        );

        List<ProofProblemDAO> result = userRepository.saveAll(users);
        result.forEach(System.out::println);

    }

    private String verifyFormula(String formula, boolean isFOL) throws Exception {
        return isFOL ? LogicAPI.parseFOL(formula).toString() : LogicAPI.parsePL(formula).toString();
    }

    private ProofProblemDAO createNDProofProblem(String input, boolean isFOL) throws Exception {
        String[] parts = input.split(";");
        Set<String> elements = new HashSet<>();

        String conclusion = verifyFormula(parts[parts.length - 1], isFOL);
        for (int i = 0; i < parts.length - 1; i++)
            elements.add(verifyFormula(parts[i], isFOL));

        return new ProofProblemDAO(null, conclusion, elements, isFOL);
    }

}

