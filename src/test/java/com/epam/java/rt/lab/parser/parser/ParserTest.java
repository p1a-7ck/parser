package com.epam.java.rt.lab.parser.parser;

import com.epam.java.rt.lab.parser.model.Component;
import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.model.Type;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * parser
 */
public class ParserTest {

    @Test
    public void initialTest() {
        Component component = Parser.with(Ruler.from("rules.properties")).parseFile("source.txt", true);
        assertNotNull(component);
        assertNotNull(component.getType());
        assertTrue(component.getType().countSubTypes() > 0);
        assertTrue(component.countChildren() > 0);
        assertNotNull(((Composite) component).iterator(Type.of("paragraph")));
        assertNotNull(((Composite) component).iterator(Type.of("sentence")));
        assertNotNull(((Composite) component).iterator(Type.of("word")));
        assertTrue(((Composite) component).iterator(Type.of("paragraph")).hasNext());
        assertTrue(((Composite) component).iterator(Type.of("sentence")).hasNext());
        assertTrue(((Composite) component).iterator(Type.of("word")).hasNext());
        String parsedSource = component.compose(new StringBuilder()).toString();
        String readSource = readFile("source.txt");
//        for (int i = 0; i < (parsedSource.length() < readSource.length() ? parsedSource.length() : readSource.length()); i++) {
//            if (parsedSource.charAt(i) != readSource.charAt(i)) {
//                System.out.println("!= '" + parsedSource.charAt(i) + "'");
//            }
//        }
//        System.out.println("!= " + (parsedSource.length() < readSource.length() ?
//                "readSource > '" + readSource.substring(parsedSource.length(), readSource.length()) + "'" :
//                "parsedSource > '" + parsedSource.substring(readSource.length(), parsedSource.length()) + "'"));
        assertTrue(parsedSource.length() == readSource.length());
        assertTrue(parsedSource.equals(readSource));
    }

    private String readFile(String fileName) {
        StringBuilder lines = new StringBuilder();
        try {
            InputStream in = ParserTest.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null)
                lines.append(line).append("\n");
            return lines.append("\0").toString();
        } catch (Exception exc) {
            throw new IllegalStateException(exc);
        }

    }

}