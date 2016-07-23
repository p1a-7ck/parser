package com.epam.java.rt.lab.parser.model.dynamic;

import java.util.List;

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

    int countSymbols();

    List<Component> getComponentsByName(String name);
}
