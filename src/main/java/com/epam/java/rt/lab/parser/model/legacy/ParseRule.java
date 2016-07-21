package com.epam.java.rt.lab.parser.model.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * parser
 */
public class ParseRule implements ParseRuleContainable {
    private final static Logger PARSE_RULE_LOGGER = LoggerFactory.getLogger(ParseRule.class);
    private final ParseRuleContainable parent;
    private final String name;
    private final Pattern startPattern;
    private final Pattern endPattern;
    private final List<ParseRule> childRules;

    public ParseRule(ParseRuleContainable parent, String name, Pattern startPattern, Pattern endPattern) {
        this.parent = parent;
        this.name = name;
        this.startPattern = startPattern;
        this.endPattern = endPattern;
        this.childRules = new ArrayList<>();
        PARSE_RULE_LOGGER.info("ParseRule constructed ({}, {}, {}, {})",
                parent, name, startPattern.pattern(), endPattern.pattern());
    }

    public ParseRuleContainable getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public Pattern getStartPattern() {
        return this.startPattern;
    }

    public Pattern getEndPattern() {
        return this.endPattern;
    }

    @Override
    public int countParseRules() {
        return this.childRules.size();
    }

    @Override
    public ParseRule getParseRule(int index) {
        return this.childRules.get(index);
    }

    @Override
    public boolean addParseRule(ParseRule parseRule) {
        return this.childRules.add(parseRule);
    }

    @Override
    public String toString() {
        return "ParseRule{" +
                "name='" + this.name + '\'' +
                '}';
    }
}
