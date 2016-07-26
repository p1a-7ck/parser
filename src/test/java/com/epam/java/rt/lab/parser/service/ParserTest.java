package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.Componentable;
import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.model.Type;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * parser
 */
public class ParserTest {

    @Test
    public void initialTest() {
        Ruler ruler = Ruler.from("rules.properties");
        Composite composite = Parser.with(ruler).parseFile("simple.txt");

        for (Componentable componentable : composite.componentsList("paragraph")) {
            System.out.println("PPH: " + componentable.compose(new StringBuilder()));
        }
    }

}