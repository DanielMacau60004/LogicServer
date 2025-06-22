package com.logic.server.application;

import com.logic.api.LogicAPI;
import com.logic.feedback.FeedbackException;
import com.logic.feedback.FeedbackLevel;
import com.logic.server.data.ExpDTO;
import org.springframework.stereotype.Service;


@Service
public class ExpressionsApp {

    public ExpDTO verifyPLExpression(String expression, FeedbackLevel feedbackLevel) {
        try {
            return new ExpDTO(LogicAPI.parsePL(expression.replaceAll("^\"|\"$", "")).toString());
        } catch (FeedbackException e) {
            throw new RuntimeException(e.getFeedback(feedbackLevel));
        }
    }

    public ExpDTO verifyFOLExpression(String expression, FeedbackLevel feedbackLevel) {
        try {
            return new ExpDTO(LogicAPI.parseFOL(expression.replaceAll("^\"|\"$", "")).toString());
        } catch (FeedbackException e) {
            throw new RuntimeException(e.getFeedback(feedbackLevel));
        }
    }

}
