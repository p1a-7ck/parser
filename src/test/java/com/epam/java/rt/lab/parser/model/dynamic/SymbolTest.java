package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class SymbolTest {

    @Test
    public void initialTest() {
        Symbol symbol = new Symbol("a".charAt(0));
        assertEquals(symbol.getC(), "a".charAt(0));
        symbol.setC("b".charAt(0));
        assertEquals(symbol.getC(), "b".charAt(0));
    }

}