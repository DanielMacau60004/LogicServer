package com.logic.server.server.presentation;

import com.logic.server.server.application.ExpressionsApp;
import com.logic.server.server.data.ResponseDTO;
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
    public ResponseEntity<String> verifyPLExpression(String formula) {
        try {
            return ResponseDTO.entity(expressionsApp.verifyPLExpression(formula));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLExpression(String formula) {
        try {
            return ResponseDTO.entity(expressionsApp.verifyFOLExpression(formula));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }
}
