package com.epam.java.rt.parser.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Part {
    private final static Logger PART_LOGGER = LoggerFactory.getLogger(Part.class);
    private final Part parent;
    private final List<Part> children;
    private final String value;
    private final Rule rule;

    public Part(Part parent, List<Part> children, String value, Rule rule) {
        this.parent = parent;
        this.children = children;
        this.value = value;
        this.rule = rule;
        PART_LOGGER.info("Part constructed with ({}, {}, {}, {})", parent, children, value, rule);
    }

    public static Part create(String value, Rule rule) {
        return new Part(null, new ArrayList<>(), value, rule);
    }

    public Part getParent() {
        return this.parent;
    }

    public Part setParent(Part parent) {
        return new Part(parent, this.children, getValue(), this.rule);
    }

    public boolean isWithinChildrenBound(int index) {
        return index >= 0 && index < this.children.size();
    }

    public Part verifyOrSetParentTo(Part child) {
        if (child.getParent() != this) child = child.setParent(this);
        return child;
    }

    public Part getChild(int index) {
        if (isWithinChildrenBound(index))
            return this.children.get(index);
        return null;
    }

    public void addChild(Part child) {
        this.children.add(verifyOrSetParentTo(child));
    }

    public boolean insertChild(int index, Part child) {
        if (isWithinChildrenBound(index)) {
            this.children.add(index, verifyOrSetParentTo(child));
            return true;
        }
        return false;
    }

    public boolean setChild(int index, Part child) {
        if (isWithinChildrenBound(index)){
            this.children.set(index, verifyOrSetParentTo(child));
            return true;
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public Part setValue(String value) {
        return new Part(getParent(), this.children, value, this.rule);
    }

    @Override
    public String toString() {
        return "Part{" +
                "value='" + value + '\'' +
                '}';
    }
}
