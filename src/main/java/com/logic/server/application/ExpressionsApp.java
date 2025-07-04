package com.logic.server.application;

import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.FeedbackAPI;
import com.logic.server.data.ExpDTO;
import com.logic.server.others.Utils;
import org.springframework.stereotype.Service;


@Service
public class ExpressionsApp {

    public ExpDTO verifyPLExpression(String exp, FeedbackLevel feedbackLevel) {
        try {
            exp = Utils.formatString(exp);
            return new ExpDTO(FeedbackAPI.parsePL(exp, feedbackLevel));
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong!", e);
        }
    }

    public ExpDTO verifyFOLExpression(String exp, FeedbackLevel feedbackLevel) {
        try {
            exp = Utils.formatString(exp);
            return new ExpDTO(FeedbackAPI.parseFOL(exp, feedbackLevel));
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong!", e);
        }
    }

}
