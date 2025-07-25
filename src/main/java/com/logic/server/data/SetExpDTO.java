package com.logic.server.data;

import com.logic.api.IFormula;
import com.logic.exps.asts.IASTExp;
import com.logic.others.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SetExpDTO {

    private Set<String> exps;
    public SetExpDTO(Set<IASTExp> exps) {
        this.exps = exps.stream().map(f->Utils.getToken(f.toString())).collect(Collectors.toSet());
    }
}