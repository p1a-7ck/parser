package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.legacy.RootSegment;
import com.epam.java.rt.lab.parser.service.legacy.ParseRuleReader;
import org.junit.Test;

/**
 * parser
 */
public class ParseRuleReaderTest {

    @Test
    public void initialTest() {
        RootSegment rootSegment = new RootSegment(new StringBuilder("one two three four five six seven eight nine ten"));
        (new ParseRuleReader(rootSegment,ParseRuleReader.getProperties("parse_rules.properties"))).createParseRules();

    }

}