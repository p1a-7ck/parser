package com.epam.java.rt.parser.model;

/**
 * parser
 */
public class Manager {
    Rule rootRule;
    Part rootPart;

    public Manager() {
    }

    public Rule getRootRule() {
        return rootRule;
    }

    public void setRootRule(Rule rootRule) {
        this.rootRule = rootRule;
    }

    public Part getRootPart() {
        return rootPart;
    }

    public void setRootPart(Part rootPart) {
        this.rootPart = rootPart;
    }
}
