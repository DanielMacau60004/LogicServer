package com.logic.server.api;

public interface ComponentVisitor<T> {

    T visit(Components.ExpComponent component);
    T visit(Components.MarkComponent component);
    T visit(Components.RuleComponent component);
    T visit(Components.TreeComponent component);

}
