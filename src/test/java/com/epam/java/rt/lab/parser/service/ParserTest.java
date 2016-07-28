package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.Component;
import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.model.Type;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;

/**
 * parser
 */
public class ParserTest {

    @Test
    public void initialTest() {
        Composite composite = Parser.with(Ruler.from("rules.properties")).parseFile("simple.txt");
        assertNotNull(composite);
        Component compNext;
        Iterator it = composite.iterator(Type.of("word"));
        while(it.hasNext()) {
            compNext = (Component) it.next();
            System.out.println(compNext.getType().getName() + ": " + compNext.compose(new StringBuilder()));
        }
        System.out.println(composite.compose(new StringBuilder()));

//        for (Component item : composite) {
//            item = (Component) item;
//            System.out.println(item.getType().getName() + " = '" + item.compose(new StringBuilder()) + "'");
//        }

    }

}