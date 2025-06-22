package com.logic.server.data;

import com.logic.api.INDProof;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.server.api.Components;
import com.logic.server.api.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofDTO {

    private String conclusion;

    private Set<String> premises;
    private Map<String, String> hypotheses;
    private Components.Component proof;
    private FeedbackLevel feedbackLevel;

    private String mainException;

    private boolean hasErrors;

    public ProofDTO(INDProof proof, FeedbackException mainException, FeedbackLevel feedbackLevel, boolean hasErrors) {
        this.feedbackLevel = feedbackLevel;
        if(feedbackLevel == null)
            this.feedbackLevel = FeedbackLevel.NONE;

        this.conclusion = proof.getConclusion().toString();
        this.premises = new HashSet<>();
        this.hypotheses = new HashMap<>();
        this.proof = JsonMapper.convertToPreviewComponent(proof.getAST(), feedbackLevel);

        if(mainException != null)
            this.mainException = mainException.getFeedback(feedbackLevel);

        this.hasErrors = hasErrors;

        proof.getHypotheses().forEachRemaining(e -> {
            if (e.getKey() != null && e.getValue() != null)
                hypotheses.put(e.getKey(), e.getValue().toString());
        });
        proof.getPremises().forEachRemaining(i -> premises.add(i.toString()));
    }
}