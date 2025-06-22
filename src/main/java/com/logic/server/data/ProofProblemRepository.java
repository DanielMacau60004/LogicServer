package com.logic.server.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProofProblemRepository extends JpaRepository<ProofProblemDAO, Long> {

    @Query("SELECT DISTINCT p FROM ProofProblemDAO p WHERE p.isFOL = false")
    Page<ProofProblemDAO> getPLProblems(Pageable pageable);

    @Query("SELECT DISTINCT p FROM ProofProblemDAO p WHERE p.isFOL = true")
    Page<ProofProblemDAO> getFOLProblems(Pageable pageable);
}
