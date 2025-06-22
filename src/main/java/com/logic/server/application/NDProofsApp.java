package com.logic.server.application;

import com.logic.api.*;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.feedback.FeedbackLevel;
import com.logic.nd.algorithm.*;
import com.logic.nd.algorithm.state.strategies.HeightTrimStrategy;
import com.logic.nd.algorithm.state.strategies.SizeTrimStrategy;
import com.logic.nd.exceptions.NDException;
import com.logic.server.api.Components;
import com.logic.server.data.ProofDTO;
import com.logic.server.data.ProofProblemDAO;
import com.logic.server.data.ProofProblemDTO;
import com.logic.server.data.ProofProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class NDProofsApp {

    private final ProofProblemRepository proofProblemRepository;

    public NDProofsApp(ProofProblemRepository proofProblemRepository) {
        this.proofProblemRepository = proofProblemRepository;
    }

    public ProofProblemDTO getPLProblem(long problemNum) {
        ProofProblemDAO proofProblemDAO = proofProblemRepository.getReferenceById(problemNum);
        if (proofProblemDAO.isFOL())
            throw new NullPointerException("PL problem with id " + problemNum + " does not exist!");
        return proofProblemDAO.toDTO();
    }

    public ProofProblemDTO getFOLProblem(long problemNum) {
        ProofProblemDAO proofProblemDAO = proofProblemRepository.getReferenceById(problemNum);
        if (!proofProblemDAO.isFOL())
            throw new NullPointerException("FOL problem with id " + problemNum + " does not exist!");
        return proofProblemDAO.toDTO();
    }

    public Page<ProofProblemDTO> getPLProblems(Pageable pageable) {
        return proofProblemRepository.getPLProblems(pageable).map(ProofProblemDAO::toDTO);
    }

    public Page<ProofProblemDTO> getFOLProblems(Pageable pageable) {
        return proofProblemRepository.getFOLProblems(pageable).map(ProofProblemDAO::toDTO);
    }

    public ProofDTO verifyGeneralProblem(Components.TreeComponent tree, FeedbackLevel feedbackLevel, boolean isFOL) {
        try {
            System.out.println(tree.toString() + " " + isFOL);
            INDProof ndProof = isFOL ? LogicAPI.parseNDFOLProof(tree.toString()) : LogicAPI.parseNDPLProof(tree.toString());
            return new ProofDTO(ndProof, null, feedbackLevel, false);
        } catch (NDException e) {
            return new ProofDTO(e.getProof(), null, feedbackLevel, true);
        }
    }

    public ProofDTO verifyProblem(Components.TreeComponent tree, String[] problem, FeedbackLevel feedbackLevel, boolean isFOL) {
        if (problem.length == 0)
            throw new RuntimeException("Invalid problem!");

        try {
            IFormula conclusion = null;
            Set<IFormula> premises = new HashSet<>();

            for (int i = 0; i < problem.length; i++) {
                IFormula formula = isFOL ? LogicAPI.parseFOL(problem[i]) : LogicAPI.parsePL(problem[i]);
                if (i == problem.length - 1) conclusion = formula;
                else premises.add(formula);
            }

            assert conclusion != null;
            INDProof ndProof = isFOL ? LogicAPI.parseNDFOLProof(tree.toString()) : LogicAPI.parseNDPLProof(tree.toString());
            ndProof = LogicAPI.checkNDProblem(ndProof, premises, conclusion);

            return new ProofDTO(ndProof, null, feedbackLevel, false);
        } catch (NDException e) {
            if (e.getMainException() != null)
                return new ProofDTO(e.getProof(), e.getMainException(), feedbackLevel, true);
            return new ProofDTO(e.getProof(), null, feedbackLevel, true);
        }

    }

    public ProofDTO solvePLProblem(String[] problem, FeedbackLevel feedbackLevel) {
        IPLFormula conclusion = LogicAPI.parsePL(problem[problem.length - 1]);
        Set<IPLFormula> premises = new HashSet<>();

        for (String premise : Arrays.copyOf(problem, problem.length - 1))
            premises.add(LogicAPI.parsePL(premise));

        INDProof proof = new AlgoProofPLBuilder(
                new AlgoProofPLProblemBuilder(conclusion)
                        .addPremises(premises))
                .setAlgoSettingsBuilder(
                        new AlgoSettingsBuilder()
                                .setTimeout(1500)
                                .setHypothesesPerState(Integer.MAX_VALUE)
                                .setTotalClosedNodes(Integer.MAX_VALUE)
                                .setTrimStrategy(new SizeTrimStrategy()))
                .build();

        return new ProofDTO(proof, null, feedbackLevel, true);
    }

    public ProofDTO solveFOLProblem(String[] problem, FeedbackLevel feedbackLevel) {
        IFOLFormula conclusion = LogicAPI.parseFOL(problem[problem.length - 1]);
        Set<IFOLFormula> premises = new HashSet<>();

        for (String premise : Arrays.copyOf(problem, problem.length - 1))
            premises.add(LogicAPI.parseFOL(premise));

        INDProof proof = new AlgoProofFOLBuilder(
                new AlgoProofFOLProblemBuilder(conclusion)
                        .addPremises(premises)
                        .addTerm(new ASTVariable("w"))
        )
                .setAlgoSettingsBuilder(
                        new AlgoSettingsBuilder()
                                .setTotalClosedNodes(10000)
                                .setHypothesesPerState(4)
                                .setTimeout(1000)
                                .setTrimStrategy(new SizeTrimStrategy()))
                .build();

        return new ProofDTO(proof, null, feedbackLevel, true);
    }
}
