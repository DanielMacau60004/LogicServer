package com.logic.server.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.logic.exps.asts.IASTExp;
import com.logic.feedback.FeedbackLevel;
import com.logic.nd.ERule;
import com.logic.nd.asts.IASTND;
import com.logic.nd.exceptions.NDRuleException;
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

    // Common interface for all preview components
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

        protected void appendNDErrors(List<NDRuleException> errors, FeedbackLevel feedback) {
            for (NDRuleException e : errors) {
                List<IASTND> previews = e.getPreviews(feedback);
                List<Component> map = previews != null ? previews.stream()
                        .map(m -> JsonMapper.convertToPreviewComponent(m, feedback)).toList() : null;
                this.errors.put(e.getFeedback(feedback), map);
            }
        }

        public abstract <T> T accept(ComponentVisitor<T> visitor);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpComponent extends Component {
        public String value;
        public MarkComponent mark;

        @JsonIgnore
        protected IASTND nd;

        public ExpComponent(IASTExp value) {
            super("EXP");
            this.value = Utils.getToken(value.toString());
        }

        public ExpComponent(IASTND nd, IASTExp value, String mark) {
            super("EXP");
            this.nd = nd;
            this.value = Utils.getToken(value.toString());
            this.mark = new MarkComponent(mark);
        }

        @Override
        public String toString() {
            String str = value + ".";
            if (mark != null) return "[H," + mark + "] [" + str + "]";
            return str;
        }

        @Override
        public <T> T accept(ComponentVisitor<T> visitor) {
            return visitor.visit(this);
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
        public <T> T accept(ComponentVisitor<T> visitor) {
            return visitor.visit(this);
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
        public <T> T accept(ComponentVisitor<T> visitor) {
            return visitor.visit(this);
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

        @JsonIgnore
        protected IASTND nd;

        public TreeComponent(
                IASTND nd,
                IASTExp conclusion,
                ERule rule,
                List<String> marks,
                List<Component> hypotheses
        ) {
            super("TREE");
            this.conclusion = new ExpComponent(conclusion);
            this.marks = marks.stream().map(MarkComponent::new).toList();
            this.rule = new RuleComponent(rule);
            this.hypotheses = hypotheses;
            this.nd = nd;
        }

        @Override
        public <T> T accept(ComponentVisitor<T> visitor) {
            return visitor.visit(this);
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
