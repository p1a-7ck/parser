package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * parser
 */
public class Symbol {
    private static final Logger LOGGER_SYMBOL = LoggerFactory.getLogger(Symbol.class);
    private char c;

    public Symbol() {
        LOGGER_SYMBOL.info("Symbol constructed");
    }

    public Symbol(char c) {
        this.c = c;
        LOGGER_SYMBOL.info("Symbol({}) constructed", c);
    }

    public static Symbol of(char c) {
        return new Symbol(c);
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
        LOGGER_SYMBOL.info("Symbol char set of '{}'", c);
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "char=" + this.c +
                '}';
    }
}
