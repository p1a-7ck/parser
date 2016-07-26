package com.epam.java.rt.lab.parser.service;

import org.junit.Test;

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
        System.out.println(parser.getComposite().compose(new StringBuilder()));
    }

}