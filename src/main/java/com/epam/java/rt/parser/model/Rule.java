package com.epam.java.rt.parser.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Rule {
    private final static Logger RULE_LOGGER = LoggerFactory.getLogger(Rule.class);
    private final Rule parent;
    private final List<Rule> children;
    private final String value;

    public Rule(Rule parent, List<Rule> children, String value) {
        this.parent = parent;
        this.children = children;
        this.value = value;
        RULE_LOGGER.info("Rule constructed with ({}, {}, {})", parent, children, value);
    }

    public static Rule create(String value) {
        return new Rule(null, new ArrayList<>(), value);
    }

    public Rule getParent() {
        return this.parent;
    }

    public Rule setParent(Rule parent) {
        return new Rule(parent, this.children, getValue());
    }

    public boolean isWithinChildrenBound(int index) {
        return index >= 0 && index < this.children.size();
    }

    public Rule verifyOrSetParentTo(Rule child) {
        if (child.getParent() != this) child = child.setParent(this);
        return child;
    }

    public Rule getChild(int index) {
        if (isWithinChildrenBound(index))
            return this.children.get(index);
        return null;
    }

    public void addChild(Rule child) {
        this.children.add(verifyOrSetParentTo(child));
    }

    public boolean insertChild(int index, Rule child) {
        if (isWithinChildrenBound(index)) {
            this.children.add(index, verifyOrSetParentTo(child));
            return true;
        }
        return false;
    }

    public boolean setChild(int index, Rule child) {
        if (isWithinChildrenBound(index)) {
            this.children.set(index, verifyOrSetParentTo(child));
            return true;
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public Rule setValue(String value) {
        return new Rule(getParent(), this.children, value);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "value='" + value + '\'' +
                '}';
    }
}
