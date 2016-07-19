package com.epam.java.rt.parser.model;

/**
 * parser
 */
public interface ParseRuleContainable {
    int countParseRules();
    ParseRule getParseRule(int index);
    boolean addParseRule(ParseRule parseRule);
}
