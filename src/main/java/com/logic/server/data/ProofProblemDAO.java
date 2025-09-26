package com.logic.server.data;

import com.logic.server.others.StringSetConverter;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "nd_problem", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"conclusion", "premises", "isFOL"})
})
public class ProofProblemDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conclusion", nullable = false)
    private String conclusion;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "premises", length = 1000)
    private Set<String> premises;

    @Column(name = "isFOL")
    private boolean isFOL;

    public ProofProblemDAO() {}

    public ProofProblemDAO(Long id, String conclusion, Set<String> premises, boolean isFOL) {
        this.id = id;
        this.conclusion = conclusion;
        this.premises = premises;
        this.isFOL = isFOL;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Set<String> getPremises() {
        return premises;
    }

    public void setPremises(Set<String> premises) {
        this.premises = premises;
    }

    public boolean isFOL() {
        return isFOL;
    }

    public void setFOL(boolean isFOL) {
        this.isFOL = isFOL;
    }

    public ProofProblemDTO toDTO() {
        return new ProofProblemDTO(id, conclusion, premises, isFOL);
    }

    @Override
    public String toString() {
        return id + ", " + premises + ", " + conclusion + ", " + isFOL;
    }
}
