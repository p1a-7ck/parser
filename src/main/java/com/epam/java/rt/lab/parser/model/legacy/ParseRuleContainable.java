package com.epam.java.rt.lab.parser.model.legacy;

/**
 * parser
 */
public interface ParseRuleContainable {
    int countParseRules();
    ParseRule getParseRule(int index);
    boolean addParseRule(ParseRule parseRule);
}
