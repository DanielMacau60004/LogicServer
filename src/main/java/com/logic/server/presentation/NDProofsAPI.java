package com.logic.server.presentation;

import com.logic.feedback.FeedbackLevel;
import com.logic.server.api.Components;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/nd")
public interface NDProofsAPI {

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
    ResponseEntity<String> getPLProblems(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size);

    @GetMapping(
            value = "/fol/problem",
            produces = "application/json"
    )
    ResponseEntity<String> getFOLProblems(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size);

    @PostMapping(
            value = "/pl",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<String> verifyGeneralPLProblem(
            @RequestBody Components.TreeComponent tree,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @PostMapping(
            value = "/fol",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<String> verifyGeneralFOLProblem(
            @RequestBody Components.TreeComponent tree,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @PostMapping(
            value = "/pl/problem",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLProblem(
            @RequestBody Components.TreeComponent tree,
            @RequestParam("problem") String[] problem,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @PostMapping(
            value = "/fol/problem",
            consumes = "application/json",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLProblem(
            @RequestBody Components.TreeComponent tree,
            @RequestParam("problem") String[] problem,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @GetMapping(
            value = "/pl/problem/solve",
            produces = "application/json"
    )
    ResponseEntity<String> solvePLProblem(
            @RequestParam("problem") String[] problem);

    @GetMapping(
            value = "/fol/problem/solve",
            produces = "application/json"
    )
    ResponseEntity<String> solveFOLProblem(
            @RequestParam("problem") String[] problem);


    @PostMapping(
            value = "/pl/hint",
            produces = "application/json"
    )
    ResponseEntity<String> verifyPLHint(
            @RequestParam("problem") String[] problem,
            @RequestParam("goal") String[] goal,
            @RequestParam("level") FeedbackLevel feedbackLevel);

    @PostMapping(
            value = "/fol/hint",
            produces = "application/json"
    )
    ResponseEntity<String> verifyFOLHint(
            @RequestParam("problem") String[] problem,
            @RequestParam("goal") String[] goal,
            @RequestParam("level") FeedbackLevel feedbackLevel);
}
