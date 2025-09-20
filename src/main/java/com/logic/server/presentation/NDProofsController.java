package com.logic.server.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.feedback.FeedbackLevel;
import com.logic.server.api.Components;
import com.logic.server.application.NDProofsApp;
import com.logic.server.data.ResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class NDProofsController implements NDProofsAPI {

    private final NDProofsApp ndProofsApp;

    public NDProofsController(NDProofsApp ndProofsApp) {
        this.ndProofsApp = ndProofsApp;
    }

    @Override
    public ResponseEntity<String> getPLProblem(long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.getPLProblem(problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> getFOLProblem(long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.getFOLProblem(problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> getPLProblems(Integer page, Integer size) {
        try {
            return ResponseDTO.entity(ndProofsApp.getPLProblems(
                    PageRequest.of(page != null ? page : 0, size != null ? size : 10)));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getFOLProblems(Integer page, Integer size) {
        try {
            return ResponseDTO.entity(ndProofsApp.getFOLProblems(
                    PageRequest.of(page != null ? page : 0, size != null ? size : 10)));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyGeneralPLProblem(Components.TreeComponent tree, FeedbackLevel feedbackLevel) {
        try {
            System.out.println(new ObjectMapper().writeValueAsString(tree));
            return ResponseDTO.entity(ndProofsApp.verifyGeneralProblem(tree, feedbackLevel, false));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyGeneralFOLProblem(Components.TreeComponent tree, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyGeneralProblem(tree, feedbackLevel, true));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyPLProblem(Components.TreeComponent tree, String[] problem, FeedbackLevel feedbackLevel) {
        try {
            var a = ResponseDTO.entity(ndProofsApp.verifyProblem(tree, problem, feedbackLevel, false));
            System.out.println(a.getBody());
            return a;
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLProblem(Components.TreeComponent tree, String[] problem, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyProblem(tree, problem, feedbackLevel, true));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> solvePLProblem(String[] problem) {
        try {
            return ResponseDTO.entity(ndProofsApp.solvePLProblem(problem));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> solveFOLProblem(String[] problem) {
        try {
            return ResponseDTO.entity(ndProofsApp.solveFOLProblem(problem));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyPLHint(String[] problem, String[] goal, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(ndProofsApp.genHint(problem, goal, feedbackLevel, false));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLHint(String[] problem, String[] goal, FeedbackLevel feedbackLevel) {
        try {
            return ResponseDTO.entity(ndProofsApp.genHint(problem, goal, feedbackLevel, true));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> expsPL(String[] exps) {
        try {
            return ResponseDTO.entity(ndProofsApp.listPossibleFormulas(exps, false));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> expsFOL(String[] exps) {
        try {
            return ResponseDTO.entity(ndProofsApp.listPossibleFormulas(exps, true));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }
}
