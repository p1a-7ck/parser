package com.epam.java.rt.lab.parser.model.dynamic;

/**
 * parser
 */
public interface Composite {
    Component get(int index);

    boolean add(Component component);

    Component remove(int index);

    int count();

    StringBuilder compose(StringBuilder stringBuilder);

    Rule getRule();

    void setRule(Rule rule);
}
