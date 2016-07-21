package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Composite;
import com.epam.java.rt.lab.parser.model.dynamic.Text;
import org.junit.Test;

/**
 * parser
 */
public class ParserTest {

    @Test
    public void initialTest() {
        Composite text = new Text();
        Ruler.from("rules.properties").setRules(text);
        Parser.to(text).parseFile("source.txt");

    }

}