package com.logic.server.data;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.INDProofFeedback;
import com.logic.server.api.Components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProofDTO {

    private String conclusion;
    private Set<String> premises;
    private Map<String, String> hypotheses;
    private Components.Component proof;
    private FeedbackLevel feedbackLevel;
    private boolean hasError;

    public ProofDTO() {}

    public ProofDTO(String conclusion, Set<String> premises, Map<String, String> hypotheses,
                    Components.Component proof, FeedbackLevel feedbackLevel, boolean hasError) {
        this.conclusion = conclusion;
        this.premises = premises;
        this.hypotheses = hypotheses;
        this.proof = proof;
        this.feedbackLevel = feedbackLevel;
        this.hasError = hasError;
    }

    public ProofDTO(INDProofFeedback proofFeedback) {
        this.feedbackLevel = proofFeedback.getFeedbackLevel();
        this.premises = new HashSet<>();
        this.hypotheses = new HashMap<>();
        this.proof = Components.createComponent(proofFeedback.getFeedback());
        this.hasError = proofFeedback.hasError();

        if (proofFeedback.getProof() != null) {
            this.conclusion = proofFeedback.getProof().getConclusion().toString();

            proofFeedback.getProof().getHypotheses().forEachRemaining(e -> {
                if (e.getKey() != null && e.getValue() != null) {
                    this.hypotheses.put(e.getKey(), e.getValue().toString());
                }
            });

            proofFeedback.getProof().getPremises().forEachRemaining(i -> this.premises.add(i.toString()));
        }
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

    public Map<String, String> getHypotheses() {
        return hypotheses;
    }

    public void setHypotheses(Map<String, String> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public Components.Component getProof() {
        return proof;
    }

    public void setProof(Components.Component proof) {
        this.proof = proof;
    }

    public FeedbackLevel getFeedbackLevel() {
        return feedbackLevel;
    }

    public void setFeedbackLevel(FeedbackLevel feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}
