package com.epam.java.rt.parser.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class PartTest {

    @Test
    public void initialTest() {
        Rule rule = Rule.create("\\w\\s");
        Rule ruleElse = Rule.create("\\w\\s");

        Part part = Part.create("Word", rule);
        part.addChild(Part.create("W", ruleElse));
        
    }

}