package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class ElementaryComponentTest {

    @Test
    public void initialTest() {
        ElementaryComponent elementaryComponent = new ElementaryComponent();
        assertEquals(elementaryComponent.countSymbols(), 0);
        elementaryComponent.addSymbol(Symbol.of("a".charAt(0)));
        assertEquals(elementaryComponent.getChar(0), "a".charAt(0));
        assertEquals(elementaryComponent.getSymbol(0).getC(), "a".charAt(0));
        elementaryComponent.addSymbol(Symbol.of("b".charAt(0)));
        elementaryComponent.addSymbol(Symbol.of("c".charAt(0)));
        assertTrue(elementaryComponent.compose(new StringBuilder()).toString().equals("abc"));
        elementaryComponent.removeSymbol(1);
        assertTrue(elementaryComponent.compose(new StringBuilder()).toString().equals("ac"));
        Rule rule = new Rule();
        rule.setName("word");
        elementaryComponent.setRule(rule);
        assertEquals(elementaryComponent.getRule(), rule);
        Rule newRule = new Rule();
        newRule.setName("sentence");
        elementaryComponent.setRule(newRule);
        assertNotEquals(elementaryComponent.getRule(), newRule);
    }

}