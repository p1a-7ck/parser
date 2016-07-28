package com.epam.java.rt.lab.parser.model;

/**
 * parser
 */
public interface Component {
    Type getType();

    void addChars(char[] chars);

    void removeChars();

    int countChars();

    char[] getChars();

    StringBuilder compose(StringBuilder stringBuilder);
}
