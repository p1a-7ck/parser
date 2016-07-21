package com.epam.java.rt.lab.parser.model.dynamic;

import org.junit.Test;

/**
 * parser
 */
public class ModelTest {

    @Test
    public void initialTest() {
        (new RuleTest()).initialTest();
        (new SymbolTest()).initialTest();
        (new ElementaryComponentTest()).initialTest();
        (new CompoundComponentTest()).initialTest();
        (new TextTest()).initialTest();
    }
}
