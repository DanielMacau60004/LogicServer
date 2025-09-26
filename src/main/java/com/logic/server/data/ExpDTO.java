package com.logic.server.data;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.IFormulaFeedback;
import com.logic.server.api.Components;

import java.util.Set;

public class ExpDTO {

    private Components.Component exp;
    private FeedbackLevel feedbackLevel;
    private boolean hasError;

    public ExpDTO() {}

    public ExpDTO(Components.Component exp, FeedbackLevel feedbackLevel, boolean hasError) {
        this.exp = exp;
        this.feedbackLevel = feedbackLevel;
        this.hasError = hasError;
    }

    public ExpDTO(IFormulaFeedback formulaFeedback) {
        this.exp = Components.createComponent(formulaFeedback.getExpFeedback());
        this.feedbackLevel = formulaFeedback.getFeedbackLevel();
        this.hasError = formulaFeedback.hasError();
    }

    public Components.Component getExp() {
        return exp;
    }

    public void setExp(Components.Component exp) {
        this.exp = exp;
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
