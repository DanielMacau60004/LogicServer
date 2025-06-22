package com.logic.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofProblemDTO {

    private long id;

    private String conclusion;

    private Set<String> premises;

    private boolean isFOL;

    public ProofProblemDAO toDAO() {
        return new ProofProblemDAO(id, conclusion, premises, isFOL);
    }

}