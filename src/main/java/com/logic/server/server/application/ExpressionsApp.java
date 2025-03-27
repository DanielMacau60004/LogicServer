package com.logic.server.server.application;

import com.logic.api.LogicAPI;
import com.logic.server.server.data.ExpDTO;
import org.springframework.stereotype.Service;

@Service
public class ExpressionsApp {

    public ExpDTO verifyPLExpression(String expression) throws Exception {
        return new ExpDTO(LogicAPI.parsePL(expression).toString());
    }

    public ExpDTO verifyFOLExpression(String expression) throws Exception {
        return new ExpDTO(LogicAPI.parseFOL(expression).toString());
    }

}
