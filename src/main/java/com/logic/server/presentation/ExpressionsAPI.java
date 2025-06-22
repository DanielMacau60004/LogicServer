package com.logic.server.presentation;

import com.logic.feedback.FeedbackLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/exp")
public interface ExpressionsAPI {

    @PostMapping(
            value = "/pl",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLExpression(
            @RequestBody String formula,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @PostMapping(
            value = "/fol",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLExpression(
            @RequestBody String formula,
            @RequestParam("level") FeedbackLevel feedbackLevel);

}
