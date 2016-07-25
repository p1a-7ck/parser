package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;

/**
 * parser
 */
public class Parser {
    private final static Logger LOGGER_PARSER = LoggerFactory.getLogger(Parser.class);
    private Composite composite;
    private StringBuilder source;
    private boolean ignoreMissed = false;

    public Parser() {
    }

    public static Parser to(Composite composite) {
        Parser parser = new Parser();
        parser.composite = composite;
        return parser;
    }

    public void parseFile(String fileName) {
        StringBuilder lines = new StringBuilder();
        try {
            InputStream in = Parser.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            LOGGER_PARSER.info("Parser file '{}' found", fileName);
            while ((line = reader.readLine()) != null)
                lines.append(line).append("\n");
            this.ignoreMissed = ignoreMissed;
            parse(lines);
        } catch (Exception exc) {
            LOGGER_PARSER.error("Parser not found file '{}' or file read error", fileName, exc);
        }
    }

    public int findRuleStart(int findFrom, Rule rule) {
        Matcher matcher = rule.getPatternStart().matcher(this.source);
        if (!matcher.find(findFrom)) return -1;
        return matcher.start();
    }

    public Component getRuleComponent(int ruleStart, Rule rule) {
        Matcher matcher = rule.getPatternEnd().matcher(this.source);
        if (!matcher.find(ruleStart)) {
            int ruleSomeEnd = ruleStart + 15;
            if (ruleSomeEnd > this.source.length()) ruleSomeEnd = this.source.length();
            throw new IllegalStateException("PatternEnd not found for rule '" + rule.getName() + "'" +
                    " (" + this.source.substring(ruleStart, ruleSomeEnd) + "...)");
        }
        Component component = new ElementaryComponent(rule);
        component.addChars(this.source.substring(ruleStart, matcher.start()).toCharArray());
        return component;
    }

    public int parseComposite(int findFrom, Composite composite, boolean parentEndsWith) {
        int recursiveIndex;
        int findIndex = findFrom;
        Rule childRule;
        Component component;
        LOGGER_PARSER.debug("parseComposite ({}, {}, {})", findFrom, composite, parentEndsWith);
        if (composite.getRule().getStartsWith() != null) {
            LOGGER_PARSER.debug("{}.getStartsWith() !== null ({})",
                    composite.getRule(), composite.getRule().getStartsWith().getPatternStart().toString());
            findIndex = findRuleStart(findIndex, composite.getRule().getStartsWith());
            if (findIndex > findFrom || findIndex < 0) return -1;
            component = getRuleComponent(findIndex, composite.getRule().getStartsWith());
            composite.add(component);
            findIndex += component.countSymbols();
        }
        do {
            findFrom = findIndex;
            LOGGER_PARSER.debug("do (findFrom = {}, findIndex = {})", findFrom, findIndex);
            if (composite.getRule().getEndsWith() != null) {
                LOGGER_PARSER.debug("{}.getEndsWith() !== null ({})",
                        composite.getRule(), composite.getRule().getEndsWith().getPatternStart().toString());
                if (findIndex == findRuleStart(findIndex, composite.getRule().getEndsWith())) {
                    component = getRuleComponent(findIndex, composite.getRule().getEndsWith());
                    composite.add(component);
                    findIndex += component.countSymbols();
                    return findIndex;
                }
            }
            LOGGER_PARSER.debug("for (int i = 0; i < {}; i++) (.getRule() = {})", composite.getRule().countRules(), composite.getRule());
            for (int i = 0; i < composite.getRule().countRules(); i++) {
                childRule = composite.getRule().getRule(i);
                LOGGER_PARSER.debug("childRule = {}", childRule);
                if (childRule.countRules() > 0) {
                    component = new CompoundComponent(childRule);
                    recursiveIndex = parseComposite(findIndex, (Composite) component,
                            composite.getRule().getEndsWith() != null || parentEndsWith);
                    if (findIndex < recursiveIndex) {
                        composite.add(component);
                        findIndex = recursiveIndex;
                    }
                } else {
                    if (findIndex == findRuleStart(findIndex, childRule)) {
                        component = getRuleComponent(findIndex, childRule);
                        composite.add(component);
                        findIndex += component.countSymbols();
                    }
                }
                LOGGER_PARSER.debug("if ({} > {} && {}) break;", findIndex, findFrom, parentEndsWith);
                if (findIndex > findFrom && parentEndsWith) break;
            }
            LOGGER_PARSER.debug("while ({} > {} && !{}) break;", findIndex, findFrom, parentEndsWith);
        } while (findIndex > findFrom && !parentEndsWith);
        if (findIndex == findFrom && composite.getRule().getEndsWith() != null) {
            int someIndex = findIndex + 15;
            if (someIndex > this.source.length()) someIndex = this.source.length();
            if (!this.ignoreMissed) {
                throw new IllegalStateException("Parser found missed components (" +
                        this.source.substring(findIndex, someIndex) + "...)" +
                        " " + composite.getRule());
            } else {
                LOGGER_PARSER.warn("Parser found missed components ({}) {}",
                        this.source.substring(findIndex, someIndex), composite.getRule());
            }
        }
        LOGGER_PARSER.debug("parseComposite.return = {}", findIndex);
        return findIndex;
    }

    public void parse(StringBuilder source) {
        this.source = source;
        parseComposite(0, this.composite, false);
    }

    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public boolean isIgnoreMissed() {
        return ignoreMissed;
    }

    public void setIgnoreMissed(boolean ignoreMissed) {
        this.ignoreMissed = ignoreMissed;
    }
}