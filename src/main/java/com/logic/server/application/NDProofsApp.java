package com.logic.server.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logic.api.*;
import com.logic.exps.asts.others.ASTVariable;
import com.logic.feedback.FeedbackLevel;
import com.logic.feedback.api.FeedbackAPI;
import com.logic.feedback.api.INDProofFeedback;
import com.logic.feedback.nd.NDFeedbacks;
import com.logic.feedback.nd.algorithm.*;
import com.logic.feedback.nd.algorithm.proofs.strategies.SizeTrimStrategy;
import com.logic.feedback.nd.hints.Hints;
import com.logic.others.Utils;
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
        System.out.println(tree.toString() + " " + isFOL);
        INDProofFeedback ndProof = isFOL ? FeedbackAPI.parseNDFOL(tree.toString(), feedbackLevel)
                : FeedbackAPI.parseNDPL(tree.toString(), feedbackLevel);

        return new ProofDTO(ndProof);
    }

    public ProofDTO verifyProblem(Components.TreeComponent tree, String[] problem, FeedbackLevel feedbackLevel, boolean isFOL) {
        if (problem.length == 0)
            throw new RuntimeException("Invalid problem!");

        IFormula conclusion = null;
        Set<IFormula> premises = new HashSet<>();

        for (int i = 0; i < problem.length; i++) {
            IFormula formula = isFOL ? LogicAPI.parseFOL(problem[i]) : LogicAPI.parsePL(problem[i]);
            if (i == problem.length - 1) conclusion = formula;
            else premises.add(formula);
        }

        try {
            System.out.println(Utils.getToken(new ObjectMapper().writeValueAsString(tree)));
            System.out.println(Utils.getToken(tree.toString()));
        } catch (Exception e) {
        }

        INDProofFeedback ndProof = isFOL ? FeedbackAPI.parseNDFOLProblem(tree.toString(), feedbackLevel, premises, conclusion)
                : FeedbackAPI.parseNDPLProblem(tree.toString(), feedbackLevel, premises, conclusion);

        return new ProofDTO(ndProof);
    }

    public ProofDTO solvePLProblem(String[] problem) {
        IPLFormula conclusion = LogicAPI.parsePL(problem[problem.length - 1]);
        Set<IPLFormula> premises = new HashSet<>();

        for (String premise : Arrays.copyOf(problem, problem.length - 1))
            premises.add(LogicAPI.parsePL(premise));

        INDProof proof = new AlgoProofPLBuilder(
                new AlgoProofPLMainGoalBuilder(conclusion)
                        .addPremises(premises))
                .setAlgoSettingsBuilder(
                        new AlgoSettingsBuilder()
                                .setTimeout(2000)
                                .setHypothesesPerGoal(Integer.MAX_VALUE)
                                .setTotalClosedNodes(Integer.MAX_VALUE)
                                .setTrimStrategy(new SizeTrimStrategy()))
                .build();

        return new ProofDTO(NDFeedbacks.parseNDPLFeedback(proof.getAST(), FeedbackLevel.NONE));
    }

    public ProofDTO solveFOLProblem(String[] problem) {
        IFOLFormula conclusion = LogicAPI.parseFOL(problem[problem.length - 1]);
        Set<IFOLFormula> premises = new HashSet<>();

        for (String premise : Arrays.copyOf(problem, problem.length - 1))
            premises.add(LogicAPI.parseFOL(premise));

        INDProof proof = new AlgoProofFOLBuilder(
                new AlgoProofFOLMainGoalBuilder(conclusion)
                        .addPremises(premises)
                        .addTerm(new ASTVariable("w"))
        )
                .setAlgoSettingsBuilder(
                        new AlgoSettingsBuilder()
                                .setTotalClosedNodes(10000)
                                .setHypothesesPerGoal(4)
                                .setTimeout(2000)
                                .setTrimStrategy(new SizeTrimStrategy()))
                .build();

        return new ProofDTO(NDFeedbacks.parseNDFOLFeedback(proof.getAST(), FeedbackLevel.NONE));
    }

    public String genHint(String[] problem, String[] goal, FeedbackLevel feedbackLevel, boolean isFOL) {
        IFormula mainConclusion = isFOL ? LogicAPI.parseFOL(problem[problem.length - 1]) :
                LogicAPI.parsePL(problem[problem.length - 1]);
        Set<IFormula> mainPremises = new HashSet<>();

        for (String premise : Arrays.copyOf(problem, problem.length - 1))
            mainPremises.add(isFOL ? LogicAPI.parseFOL(premise) : LogicAPI.parsePL(premise));


        for(int i =0 ; i < goal.length; i++)
            System.out.println(goal[i]);
        try {
            LogicAPI.parseFOL(goal[0]);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        IFormula goalConclusion = isFOL ? LogicAPI.parseFOL(goal[goal.length - 1]) :
                LogicAPI.parsePL(goal[goal.length - 1]);
        Set<IFormula> goalPremises = new HashSet<>();


        for (String premise : Arrays.copyOf(goal, goal.length - 1))
            goalPremises.add(isFOL ? LogicAPI.parseFOL(premise) : LogicAPI.parsePL(premise));

        return Hints.generateHint(mainConclusion, mainPremises, goalConclusion, goalPremises, feedbackLevel, isFOL);
    }

}
