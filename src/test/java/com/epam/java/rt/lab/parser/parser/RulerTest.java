package com.epam.java.rt.lab.parser.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class RulerTest {

    @Test
    public void initialTest() {
        Ruler ruler = Ruler.from("rules.properties");
        assertNotNull(ruler);
        System.out.println(ruler.toDetails());
    }

}