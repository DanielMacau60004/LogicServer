package com.logic.server.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofProblemDTO {

    private String conclusion;

    private Set<String> premises;

    private boolean isFOL;

    public ProofProblemDAO toDAO() {
        return new ProofProblemDAO(0L, conclusion, premises, isFOL);
    }

}