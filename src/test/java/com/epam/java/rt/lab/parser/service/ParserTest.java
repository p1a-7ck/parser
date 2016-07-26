package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.Componentable;
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
        Parser parser = Parser.with(ruler);
        parser.parseFile("simple.txt");
        List<Componentable> list = parser.getComposite().componentsList("paragraph");
        for (Componentable componentable : list) {
            System.out.println("PPH: " + componentable.compose(new StringBuilder()));
        }
    }

}