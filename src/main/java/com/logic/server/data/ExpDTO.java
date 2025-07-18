package com.logic.server.data;

import com.logic.exps.ExpUtils;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.IFormulaFeedback;
import com.logic.feedback.api.INDProofFeedback;
import com.logic.server.api.Components;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpDTO {

    private Components.Component exp;
    private FeedbackLevel feedbackLevel;
    private boolean hasError;

    public ExpDTO(IFormulaFeedback formulaFeedback) {
        this.exp = Components.createComponent(formulaFeedback.getExpFeedback());
        this.feedbackLevel = formulaFeedback.getFeedbackLevel();
        this.hasError = formulaFeedback.hasError();
    }
}