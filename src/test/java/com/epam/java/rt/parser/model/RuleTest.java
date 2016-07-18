package com.epam.java.rt.parser.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class RuleTest {

    @Test
    public void initialTest() {
        Rule rule = Rule.create("\\w\\s");
        assertNotNull(rule);
        assertEquals(rule.getValue(), "\\w\\s");
        rule.addChild(Rule.create("[a-zA-Z]\\s"));
        assertNotNull(rule.getChild(0));
        assertEquals(rule.getChild(0).getValue(), "[a-zA-Z]\\s");
        rule.setChild(0, rule.getChild(0).setValue("\\w\\s"));
        assertEquals(rule.getChild(0).getValue(), "\\w\\s");

    }
}