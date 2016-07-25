package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Component;
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
        System.out.println(text.getRule().toDetails(new StringBuilder()));
//        try {
//            Parser.to(text).parse("one two three four five six seven eight nine ten");
//        } catch (Exception exc) {
//            System.out.println("'" + text.compose(new StringBuilder()) + "'");
//        }
        Parser parser = Parser.to(text);
        parser.setIgnoreMissed(true);
        parser.parse(new StringBuilder("one two three\nfour five six\nseven eight nine\nten"));
//        parser.parseFile("source.txt");
//        System.out.println("'" + text.compose(new StringBuilder()) + "'");
//        System.out.println(text.countSymbols());
        for (Component component : text.getComponentsByName("paragraph")) {
            System.out.println("=" + component.compose(new StringBuilder()));
        }

    }

}