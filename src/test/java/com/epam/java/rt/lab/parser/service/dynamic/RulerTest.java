package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Composite;
import com.epam.java.rt.lab.parser.model.dynamic.Text;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * parser
 */
public class RulerTest {

    @Test
    public void initialTest() {
        Composite text = new Text();
        Ruler.from("rules.properties").setRules(text);
        assertNotNull(text.getRule());
        assertTrue(text.getRule().countRules() > 0);
        System.out.println(text.getRule().toDetails(new StringBuilder()));
    }
}