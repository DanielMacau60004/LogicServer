package com.logic.server.server.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "nd_problem", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"conclusion", "premises", "isFOL"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProofProblemDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "conclusion", nullable = false)
    private String conclusion;

    @Convert(converter = StringSetConverter.class)
    @Column(name = "premises", length = 1000)
    private Set<String> premises;

    @Column(name = "isfOL")
    private boolean isFOL;

    public ProofProblemDTO toDTO() {
        return new ProofProblemDTO(id, conclusion, premises, isFOL);
    }

    public String toString() {
        return id + ", " + premises + ", " + conclusion + ", " + isFOL;
    }

}

