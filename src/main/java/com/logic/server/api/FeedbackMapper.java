package com.logic.server.api;


import com.logic.feedback.FeedbackLevel;
import com.logic.nd.asts.IASTND;
import com.logic.nd.asts.INDVisitor;
import com.logic.nd.asts.binary.ASTEExist;
import com.logic.nd.asts.binary.ASTEImp;
import com.logic.nd.asts.binary.ASTENeg;
import com.logic.nd.asts.binary.ASTIConj;
import com.logic.nd.asts.others.ASTEDis;
import com.logic.nd.asts.others.ASTHypothesis;
import com.logic.nd.asts.unary.*;
import com.logic.nd.exceptions.EFeedbackPosition;
import com.logic.server.api.Components.Component;
import com.logic.server.api.Components.ExpComponent;
import com.logic.server.api.Components.TreeComponent;
import org.antlr.v4.runtime.tree.Tree;

import java.util.Arrays;

public class FeedbackMapper implements  ComponentVisitor<Component> {

    private final FeedbackLevel feedbackLevel;

    FeedbackMapper(FeedbackLevel feedbackLevel) {
        this.feedbackLevel = feedbackLevel;
    }

    @Override
    public Component visit(ExpComponent exp) {
        exp.appendNDErrors(exp.nd.getErrors(), feedbackLevel);
        return exp;
    }

    @Override
    public Component visit(Components.MarkComponent component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component visit(Components.RuleComponent component) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component visit(TreeComponent tree) {
        tree.appendNDErrors(
                tree.nd.getErrors().stream().filter(e->e.getPosition()
                        .equals(EFeedbackPosition.RULE)).toList(),feedbackLevel);
        tree.conclusion.appendNDErrors(
                tree.nd.getErrors().stream().filter(e->e.getPosition()
                        .equals(EFeedbackPosition.CONCLUSION)).toList(), feedbackLevel);
        tree.hypotheses.forEach(h-> h.accept(this));
        return tree;
    }

}
