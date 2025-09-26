package com.logic.server.data;

import com.logic.api.IFOLFormula;
import com.logic.api.IPLFormula;
import com.logic.api.LogicAPI;

import java.util.Set;
import java.util.TreeSet;

public class ProofProblemDTO {

    private long id;
    private String conclusion;
    private Set<String> premises;
    private boolean isFOL;
    private Set<String> symbols;

    public ProofProblemDTO() {}

    public ProofProblemDTO(long id, String conclusion, Set<String> premises, boolean isFOL) {
        this.id = id;
        this.conclusion = conclusion;
        this.premises = premises;
        this.isFOL = isFOL;

        symbols = new TreeSet<>();

        if (!isFOL) {
            IPLFormula formula = LogicAPI.parsePL(conclusion);
            formula.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
            formula.iterateLiterals().forEachRemaining(it -> symbols.add(it.getName()));

            for (String p : premises) {
                IPLFormula premise = LogicAPI.parsePL(p);
                premise.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
                premise.iterateLiterals().forEachRemaining(it -> symbols.add(it.getName()));
            }
        } else {
            IFOLFormula formula = LogicAPI.parseFOL(conclusion);
            formula.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
            formula.iterateTerms().forEachRemaining(it -> symbols.add(it.getName()));
            formula.iteratePredicates().forEachRemaining(it -> symbols.add(it.getName()));
            formula.iterateVariables().forEachRemaining(it -> symbols.add(it.getName()));

            for (String p : premises) {
                IFOLFormula premise = LogicAPI.parseFOL(p);
                premise.iterateGenerics().forEachRemaining(it -> symbols.add(it.getId()));
                premise.iterateTerms().forEachRemaining(it -> symbols.add(it.getName()));
                premise.iteratePredicates().forEachRemaining(it -> symbols.add(it.getName()));
                premise.iterateVariables().forEachRemaining(it -> symbols.add(it.getName()));
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Set<String> getPremises() {
        return premises;
    }

    public void setPremises(Set<String> premises) {
        this.premises = premises;
    }

    public boolean isFOL() {
        return isFOL;
    }

    public void setFOL(boolean isFOL) {
        this.isFOL = isFOL;
    }

    public Set<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Set<String> symbols) {
        this.symbols = symbols;
    }

    public ProofProblemDAO toDAO() {
        return new ProofProblemDAO(id, conclusion, premises, isFOL);
    }
}
