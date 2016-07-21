package com.epam.java.rt.lab.parser.model.dynamic;

/**
 * parser
 */
public interface Component {
    Symbol getSymbol(int index);

    char getChar(int index);

    boolean addSymbol(Symbol symbol);

    boolean addChar(char c);

    boolean addChars(char[] cArray);

    Symbol removeSymbol(int index);

    int countSymbols();

    StringBuilder compose(StringBuilder stringBuilder);

    Rule getRule();

    void setRule(Rule rule);
}
