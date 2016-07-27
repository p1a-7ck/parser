package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.Componentable;
import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.model.Type;
import org.junit.Test;

import java.util.Iterator;

/**
 * parser
 */
public class ParserTest {

    @Test
    public void initialTest() {
        Composite composite = Parser.with(Ruler.from("rules.properties")).parseFile("simple.txt");
        Iterator it = composite.iterator(Type.of("word"));
        while(it.hasNext()) {
            System.out.println("filtered: " + ((Componentable) it.next()).compose(new StringBuilder()));
        }

        for (Componentable item : composite) {
            item = (Componentable) item;
            System.out.println(item.getType().getName() + " = '" + item.compose(new StringBuilder()) + "'");
        }

    }

}