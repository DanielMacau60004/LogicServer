package com.logic.server.server.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/exp")
public interface ExpressionsAPI {

    @PostMapping(
            value = "/pl",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLExpression(@RequestBody String formula);

    @PostMapping(
            value = "/fol",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLExpression(@RequestBody String formula);

}
