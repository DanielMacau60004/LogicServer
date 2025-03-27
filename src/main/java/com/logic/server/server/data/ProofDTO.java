package com.logic.server.server.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofDTO {

    private String conclusion;

    private Set<String> premises;

    private String proof;

}