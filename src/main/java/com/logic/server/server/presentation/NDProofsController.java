package com.logic.server.server.presentation;

import com.logic.server.server.application.NDProofsApp;
import com.logic.server.server.data.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Pageable;

@CrossOrigin("*")
@RestController
public class NDProofsController implements NDProofsAPI{

    private final NDProofsApp ndProofsApp;

    public NDProofsController(NDProofsApp ndProofsApp) {
        this.ndProofsApp = ndProofsApp;
    }

    @Override
    public ResponseEntity<String> verifyPLExpression(String proof) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyNDPLExpression(proof));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLExpression(String proof) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyNDFOLExpression(proof));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getPLProblem(long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.getPLProblem(problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getFOLProblem(long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.getFOLProblem(problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getPLProblems(Pageable pageable) {
        try {
            return ResponseDTO.entity(ndProofsApp.getPLProblems(pageable));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> getFOLProblems(Pageable pageable) {
        try {
            return ResponseDTO.entity(ndProofsApp.getFOLProblems(pageable));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyPLProblem(String proof, long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyPLProblem(proof, problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> verifyFOLProblem(String proof, long problemNum) {
        try {
            return ResponseDTO.entity(ndProofsApp.verifyFOLProblem(proof, problemNum));
        } catch (Exception e) {
            return ResponseDTO.error(e, HttpStatus.BAD_REQUEST);
        }
    }
}
