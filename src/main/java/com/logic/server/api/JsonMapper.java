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
import com.logic.server.api.Components.Component;
import com.logic.server.api.Components.ExpComponent;
import com.logic.server.api.Components.TreeComponent;

import java.util.Arrays;

public class JsonMapper implements INDVisitor<Component, Void> {


    public static Component convertToPreviewComponent(IASTND proof, FeedbackLevel feedbackLevel) {
        return proof.accept(new JsonMapper(), null).accept(new FeedbackMapper(feedbackLevel));
    }

    @Override
    public Component visit(ASTHypothesis astHypothesis, Void unused) {
        return new ExpComponent(astHypothesis, astHypothesis.getConclusion(), astHypothesis.getM());
    }

    @Override
    public Component visit(ASTIImp astiImp, Void unused) {
        return new TreeComponent(
                astiImp,
                astiImp.getConclusion(),
                astiImp.getRule(),
                Arrays.asList(astiImp.getM()),
                Arrays.asList(astiImp.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTINeg astiNeg, Void unused) {
        return new TreeComponent(
                astiNeg,
                astiNeg.getConclusion(),
                astiNeg.getRule(),
                Arrays.asList(astiNeg.getM()),
                Arrays.asList(astiNeg.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTERConj asterConj, Void unused) {
        return new TreeComponent(
                asterConj,
                asterConj.getConclusion(),
                asterConj.getRule(),
                Arrays.asList(),
                Arrays.asList(asterConj.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTELConj astelConj, Void unused) {
        return new TreeComponent(
                astelConj,
                astelConj.getConclusion(),
                astelConj.getRule(),
                Arrays.asList(),
                Arrays.asList(astelConj.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTIRDis astirDis, Void unused) {
        return new TreeComponent(
                astirDis,
                astirDis.getConclusion(),
                astirDis.getRule(),
                Arrays.asList(),
                Arrays.asList(astirDis.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTILDis astilDis, Void unused) {
        return new TreeComponent(
                astilDis,
                astilDis.getConclusion(),
                astilDis.getRule(),
                Arrays.asList(),
                Arrays.asList(astilDis.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTAbsurdity astAbsurdity, Void unused) {
        return new TreeComponent(
                astAbsurdity,
                astAbsurdity.getConclusion(),
                astAbsurdity.getRule(),
                Arrays.asList(astAbsurdity.getM()),
                Arrays.asList(astAbsurdity.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTIConj astiConj, Void unused) {
        return new TreeComponent(
                astiConj,
                astiConj.getConclusion(),
                astiConj.getRule(),
                Arrays.asList(),
                Arrays.asList(astiConj.getHyp1().accept(this, unused),
                        astiConj.getHyp2().accept(this, unused)));
    }

    @Override
    public Component visit(ASTEDis asteDis, Void unused) {
        return new TreeComponent(
                asteDis,
                asteDis.getConclusion(),
                asteDis.getRule(),
                Arrays.asList(asteDis.getM(), asteDis.getN()),
                Arrays.asList(asteDis.getHyp1().accept(this, unused),
                        asteDis.getHyp2().accept(this, unused),
                        asteDis.getHyp3().accept(this, unused)));
    }

    @Override
    public Component visit(ASTEImp asteImp, Void unused) {
        return new TreeComponent(
                asteImp,
                asteImp.getConclusion(),
                asteImp.getRule(),
                Arrays.asList(),
                Arrays.asList(asteImp.getHyp1().accept(this, unused),
                        asteImp.getHyp2().accept(this, unused)));
    }

    @Override
    public Component visit(ASTENeg asteNeg, Void unused) {
        return new TreeComponent(
                asteNeg,
                asteNeg.getConclusion(),
                asteNeg.getRule(),
                Arrays.asList(),
                Arrays.asList(asteNeg.getHyp1().accept(this, unused),
                        asteNeg.getHyp2().accept(this, unused)));
    }

    @Override
    public Component visit(ASTEUni asteUni, Void unused) {
        return new TreeComponent(
                asteUni,
                asteUni.getConclusion(),
                asteUni.getRule(),
                Arrays.asList(),
                Arrays.asList(asteUni.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTIExist astiExist, Void unused) {
        return new TreeComponent(
                astiExist,
                astiExist.getConclusion(),
                astiExist.getRule(),
                Arrays.asList(),
                Arrays.asList(astiExist.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTIUni astiUni, Void unused) {
        return new TreeComponent(
                astiUni,
                astiUni.getConclusion(),
                astiUni.getRule(),
                Arrays.asList(),
                Arrays.asList(astiUni.getHyp().accept(this, unused)));
    }

    @Override
    public Component visit(ASTEExist asteExist, Void unused) {
        return new TreeComponent(
                asteExist,
                asteExist.getConclusion(),
                asteExist.getRule(),
                Arrays.asList(asteExist.getM()),
                Arrays.asList(asteExist.getHyp1().accept(this, unused),
                        asteExist.getHyp2().accept(this, unused)));
    }
}
