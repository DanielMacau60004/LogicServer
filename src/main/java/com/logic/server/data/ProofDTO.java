package com.logic.server.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.INDProofFeedback;
import com.logic.server.api.Components;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofDTO {

    private String conclusion;

    private Set<String> premises;
    private Map<String, String> hypotheses;
    private Components.Component proof;
    private FeedbackLevel feedbackLevel;

    private boolean hasError;

    public ProofDTO(INDProofFeedback proofFeedback) {
        this.feedbackLevel = proofFeedback.getFeedbackLevel();
        this.premises = new HashSet<>();
        this.hypotheses = new HashMap<>();

        this.proof = Components.createComponent(proofFeedback.getFeedback());
        this.hasError = proofFeedback.hasError();

        INDProof proof = proofFeedback.getProof();
        if (proof != null) {
            this.conclusion = proof.getConclusion().toString();
            proof.getHypotheses().forEachRemaining(e -> {
                if (e.getKey() != null && e.getValue() != null)
                    hypotheses.put(e.getKey(), e.getValue().toString());
            });
            proof.getPremises().forEachRemaining(i -> premises.add(i.toString()));
        }

    }

}