package com.logic.server.data;

import com.logic.exps.asts.IASTExp;
import com.logic.others.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class SetExpDTO {

    private Set<String> exps;

    public SetExpDTO() {}

    public SetExpDTO(Set<IASTExp> exps) {
        this.exps = exps.stream()
                .map(f -> Utils.getToken(f.toString()))
                .collect(Collectors.toSet());
    }

    public Set<String> getExps() {
        return exps;
    }

    public void setExps(Set<String> exps) {
        this.exps = exps;
    }
}
