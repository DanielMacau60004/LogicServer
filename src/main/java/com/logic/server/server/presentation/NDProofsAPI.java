package com.logic.server.server.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RequestMapping("/nd")
public interface NDProofsAPI {

    @PostMapping(
            value = "/pl",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLExpression(@RequestBody String proof);

    @PostMapping(
            value = "/fol",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLExpression(@RequestBody String proof);

    @GetMapping(
            value = "/pl/problem/{problemNum}",
            produces = "application/json"
    )
    ResponseEntity<String> getPLProblem(@PathVariable("problemNum") long problemNum);

    @GetMapping(
            value = "/fol/problem/{problemNum}",
            produces = "application/json"
    )
    ResponseEntity<String> getFOLProblem(@PathVariable("problemNum") long problemNum);

    @GetMapping(
            value = "/pl/problem",
            produces = "application/json"
    )
    ResponseEntity<String> getPLProblems(Pageable pageable);

    @GetMapping(
            value = "/fol/problem",
            produces = "application/json"
    )
    ResponseEntity<String> getFOLProblems(Pageable pageable);

    @PostMapping(
            value = "/pl/problem/{problemNum}",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLProblem(@RequestBody String proof, @PathVariable("problemNum") long problemNum);

    @PostMapping(
            value = "/fol/problem/{problemNum}",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLProblem(@RequestBody String proof, @PathVariable("problemNum") long problemNum);
}
