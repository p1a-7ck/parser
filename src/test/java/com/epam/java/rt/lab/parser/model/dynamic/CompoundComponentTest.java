package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class CompoundComponentTest {

    @Test
    public void initialTest() {
        Composite compoundComponent = new CompoundComponent();
        Component elementaryComponent = new ElementaryComponent();
        elementaryComponent.addChars("abc def ghi ".toCharArray());
        compoundComponent.add(elementaryComponent);
        elementaryComponent = new ElementaryComponent();
        elementaryComponent.addChars("jkl mno pqr ".toCharArray());
        compoundComponent.add(elementaryComponent);
        Composite compoundComponentChild = new CompoundComponent();
        elementaryComponent = new ElementaryComponent();
        elementaryComponent.addChars("stu vwx yz_".toCharArray());
        compoundComponentChild.add(elementaryComponent);
        compoundComponent.add((Component) compoundComponentChild);
        assertTrue(compoundComponent.compose(new StringBuilder()).toString().equals("abc def ghi jkl mno pqr stu vwx yz_"));
        Rule rule = new Rule();
        rule.setName("word");
        compoundComponent.setRule(rule);
        assertEquals(compoundComponent.getRule(), rule);
        Rule newRule = new Rule();
        newRule.setName("sentence");
        compoundComponent.setRule(newRule);
        assertNotEquals(compoundComponent.getRule(), newRule);
    }

}