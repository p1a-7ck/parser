package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class TextTest {

    @Test
    public void initialTest() {
        Text text = new Text();
        text.setRule(Rule.of("text"));
        text.add(CompoundComponent.of(Rule.of("paragraph")));
        ((Composite) text.get(0)).add(ElementaryComponent.of(Rule.of("sentence")));
        ((Composite) text.get(0)).get(0).addChars("abcde".toCharArray());
        assertNotNull(text);
        assertNotNull(text.get(0));
        assertNotNull(((Composite) text.get(0)).get(0));
        assertTrue(text.getRule().getName().equals("text"));
        assertTrue(text.get(0).getRule().getName().equals("paragraph"));
        assertTrue(((Composite) text.get(0)).get(0).getRule().getName().equals("sentence"));
        assertTrue(text.compose(new StringBuilder()).toString().equals("abcde"));
    }
}