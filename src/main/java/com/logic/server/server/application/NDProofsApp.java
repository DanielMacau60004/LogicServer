package com.logic.server.server.application;

import com.logic.api.IFormula;
import com.logic.api.INDProof;
import com.logic.api.LogicAPI;
import com.logic.server.server.data.ProofDTO;
import com.logic.server.server.data.ProofProblemDAO;
import com.logic.server.server.data.ProofProblemDTO;
import com.logic.server.server.data.ProofProblemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class NDProofsApp {

    private final ProofProblemRepository proofProblemRepository;

    public NDProofsApp(ProofProblemRepository proofProblemRepository) {
        this.proofProblemRepository = proofProblemRepository;
    }

    public ProofDTO verifyNDPLExpression(String proof) throws Exception {
        INDProof ndProof =  LogicAPI.parseNDPLProof(proof);

        Iterator<IFormula> it = ndProof.getPremises();
        Set<String> premises = new HashSet<>();
        it.forEachRemaining(i->premises.add(i.toString()));

        return new ProofDTO(ndProof.getConclusion().toString(), premises, ndProof.toString());
    }

    public ProofDTO verifyNDFOLExpression(String proof) throws Exception {
        INDProof ndProof =  LogicAPI.parseNDFOLProof(proof);

        Iterator<IFormula> it = ndProof.getPremises();
        Set<String> premises = new HashSet<>();
        it.forEachRemaining(i->premises.add(i.toString()));

        return new ProofDTO(ndProof.getConclusion().toString(), premises, ndProof.toString());
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

    public ProofDTO verifyPLProblem(String proof, long problemNum) throws Exception {
        ProofDTO ndProof = verifyNDPLExpression(proof);
        ProofProblemDTO problem = getPLProblem(problemNum);

        if (!ndProof.getConclusion().equals(problem.getConclusion()) ||
                !ndProof.getPremises().equals(problem.getPremises()))
            throw new RuntimeException("This proof doesn't solve problem " + problemNum + "!");

        return ndProof;
    }

    public ProofDTO verifyFOLProblem(String proof, long problemNum) throws Exception {
        ProofDTO ndProof = verifyNDFOLExpression(proof);
        ProofProblemDTO problem = getFOLProblem(problemNum);

        if (!ndProof.getConclusion().equals(problem.getConclusion()) ||
                !ndProof.getPremises().equals(problem.getPremises()))
            throw new RuntimeException("This proof doesn't solve problem " + problemNum + "!");

        return ndProof;
    }

    public Page<ProofProblemDTO> getPLProblems(Pageable pageable) {
        return proofProblemRepository.getPLProblems(pageable).map(ProofProblemDAO::toDTO);
    }

    public Page<ProofProblemDTO> getFOLProblems(Pageable pageable) {
        return proofProblemRepository.getFOLProblems(pageable).map(ProofProblemDAO::toDTO);
    }
}
