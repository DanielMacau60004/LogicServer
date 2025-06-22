package com.logic.server.presentation;

import com.logic.feedback.FeedbackLevel;
import com.logic.server.application.ExpressionsApp;
import com.logic.server.data.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class ExpressionsController implements ExpressionsAPI {

    private final ExpressionsApp expressionsApp;

    public ExpressionsController(ExpressionsApp expressionsApp) {
        this.expressionsApp = expressionsApp;
    }

    @Override
    public ResponseEntity<String> verifyPLExpression(String formula, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(expressionsApp.verifyPLExpression(formula, feedbackLevel));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLExpression(String formula, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(expressionsApp.verifyFOLExpression(formula, feedbackLevel));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }
}
