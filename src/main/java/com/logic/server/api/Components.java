package com.logic.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.logic.feedback.exp.IExpFeedback;
import com.logic.feedback.nd.INDFeedback;
import com.logic.nd.ERule;
import com.logic.others.Utils;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Components {

    public static Component createComponent(IExpFeedback feedback) {
        return new ExpComponent(feedback, null);
    }

    public static Component createComponent(INDFeedback feedback) {
        if (feedback.getHypotheses() == null || feedback.getHypotheses().isEmpty())
            return new ExpComponent(feedback);
        return new TreeComponent(feedback);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true
    )
    @Schema(
            discriminatorProperty = "type",
            discriminatorMapping = {
                    @DiscriminatorMapping(value = "MARK", schema = MarkComponent.class),
                    @DiscriminatorMapping(value = "EXP", schema = ExpComponent.class),
                    @DiscriminatorMapping(value = "RULE", schema = RuleComponent.class),
                    @DiscriminatorMapping(value = "TREE", schema = TreeComponent.class)
            }
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ExpComponent.class, name = "EXP"),
            @JsonSubTypes.Type(value = MarkComponent.class, name = "MARK"),
            @JsonSubTypes.Type(value = RuleComponent.class, name = "RULE"),
            @JsonSubTypes.Type(value = TreeComponent.class, name = "TREE")
    })
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class Component {
        @Getter
        public String type;
        public Map<String, List<Component>> errors;

        public Component(String type) {
            this.type = type;
            this.errors = new LinkedHashMap<>();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpComponent extends Component {
        public String value;
        public MarkComponent mark;
        public boolean genHints;
        public Map<String, String> env;

        public ExpComponent(IExpFeedback feedback, Map<String, String> env) {
            super("EXP");
            this.value = Utils.getToken(feedback.getExp());

            this.env = env;
            if (feedback.hasFeedback())
                errors.put(feedback.getFeedback(), null);

            this.genHints = feedback.canGenHints();
        }

        public ExpComponent(INDFeedback feedback) {
            super("EXP");
            this.value = Utils.getToken(feedback.getConclusion().getExp());

            if (!feedback.getMarks().isEmpty())
                this.mark = new MarkComponent(feedback.getMarks().getFirst());

            if (feedback.getConclusion().hasFeedback())
                errors.put(feedback.getConclusion().getFeedback(), feedback.getConclusion().getPreviews().stream()
                        .map(Components::createComponent).toList());

            this.env = feedback.getEnv();
            this.genHints = feedback.getConclusion().canGenHints();
        }

        @Override
        public String toString() {
            String str = value + ".";
            if (mark != null) return "[H," + mark + "] [" + str + "]";
            return str;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class MarkComponent extends Component {
        public String value;

        public MarkComponent(String value) {
            super("MARK");
            this.value = value;
        }

        @Override
        public String toString() {
            return value != null ? value : "";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleComponent extends Component {
        public String value;

        public RuleComponent(ERule value) {
            super("RULE");
            this.value = value.toString();
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TreeComponent extends Component {
        public ExpComponent conclusion;
        public List<MarkComponent> marks;
        public RuleComponent rule;
        public List<Component> hypotheses;
        public boolean genHints;
        public Map<String, String> env;

        public TreeComponent(INDFeedback feedback) {
            super("TREE");

            this.conclusion = new ExpComponent(feedback.getConclusion(), feedback.getEnv());
            this.marks = feedback.getMarks().stream().map(MarkComponent::new).collect(Collectors.toList());
            this.rule = new RuleComponent(feedback.getRule());
            this.hypotheses = feedback.getHypotheses().stream().map(Components::createComponent).toList();
            this.genHints = feedback.canGenHints();

            if (feedback.hasFeedback())
                errors.put(feedback.getFeedback(), feedback.getPreviews().stream()
                        .map(Components::createComponent).toList());
        }

        @Override
        public String toString() {
            if (marks == null || hypotheses == null || rule == null)
                return conclusion.toString();

            String strMarks = marks.isEmpty() ? "" : "," + marks.stream().map(Object::toString).collect(Collectors.joining(","));
            String strHyp = hypotheses.stream().map(Object::toString).collect(Collectors.joining());
            return "[" + rule + strMarks + "] [" + conclusion + strHyp + "]";
        }

    }
}
