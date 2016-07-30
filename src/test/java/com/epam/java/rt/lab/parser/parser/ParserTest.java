package com.epam.java.rt.lab.parser.parser;

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
        Component component = Parser.with(Ruler.from("rules.properties")).parseFile("source.txt", true);
        assertNotNull(component);
        Component compNext;
        Iterator it = ((Composite) component).iterator(Type.of("paragraph"));
        while(it.hasNext()) {
            compNext = (Component) it.next();
            System.out.println(compNext.getType().getName() + ": " + compNext.compose(new StringBuilder()));
        }
    }

}