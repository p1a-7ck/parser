package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * parser
 */
public class RuleTest {

    @Test
    public void initialTest() {
        Rule rule = new Rule();
        rule.setPatternStart(Pattern.compile("//s"));
        rule.setPatternEnd(Pattern.compile("//S"));
        rule.setName("whitespace");
        assertNotNull(rule);
        assertEquals(rule.getPatternStart().toString(), "//s");
        assertEquals(rule.getPatternEnd().toString(), "//S");
        assertEquals(rule.getName(), "whitespace");
        Rule childRule = new Rule();
        childRule.setPatternStart(Pattern.compile("//d"));
        childRule.setPatternEnd(Pattern.compile("//D"));
        childRule.setName("digit");
        rule.addRule(childRule);
        assertEquals(rule.countRules(), 1);
        assertEquals(rule.getRule(0), childRule);
    }
}