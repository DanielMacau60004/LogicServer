package com.logic.server.data;

import com.logic.api.IFOLFormula;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
public class ProofProblemDTO {

    private long id;

    private String conclusion;

    private Set<String> premises;

    private boolean isFOL;

    private Set<String> symbols;

    ProofProblemDTO(long id, String conclusion, Set<String> premises, boolean isFOL) {
        this.id = id;
        this.conclusion = conclusion;
        this.premises = premises;
        this.isFOL = isFOL;

        symbols = new TreeSet<>();

        if (!isFOL) {
            IPLFormula formula = LogicAPI.parsePL(conclusion);
            formula.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
            formula.iterateLiterals().forEachRemaining(it -> symbols.add(it.getName()));

            premises.forEach(p -> {
                IPLFormula premise = LogicAPI.parsePL(p);
                premise.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
                premise.iterateLiterals().forEachRemaining(it -> symbols.add(it.getName()));
            });
        } else {

            IFOLFormula formula = LogicAPI.parseFOL(conclusion);
            formula.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
            formula.iterateTerms().forEachRemaining(it -> symbols.add(it.getName()));
            formula.iteratePredicates().forEachRemaining(it -> symbols.add(it.getName()));
            formula.iterateVariables().forEachRemaining(it -> symbols.add(it.getName()));

            premises.forEach(p -> {
                IFOLFormula premise = LogicAPI.parseFOL(p);
                premise.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
                premise.iterateTerms().forEachRemaining(it -> symbols.add(it.getName()));
                premise.iteratePredicates().forEachRemaining(it -> symbols.add(it.getName()));
                premise.iterateVariables().forEachRemaining(it -> symbols.add(it.getName()));
            });

        }

    }

    public ProofProblemDAO toDAO() {
        return new ProofProblemDAO(id, conclusion, premises, isFOL);
    }

}