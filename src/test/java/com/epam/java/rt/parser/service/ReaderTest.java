package com.epam.java.rt.parser.service;

import com.epam.java.rt.parser.model.Manager;
import org.junit.Test;

/**
 * parser
 */
public class ReaderTest {

    @Test
    public void initialTest() {
        Manager manager = new Manager();
        Reader reader = new Reader();

        reader.createRules(manager, "");
        reader.createParts(manager, "");

    }

}