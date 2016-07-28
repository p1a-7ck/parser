package com.epam.java.rt.lab.parser.model;

import java.util.Arrays;

/**
 * parser
 */
public class Leaf implements Component {
    private Type type;
    private char[] chars;

    public Leaf() {
    }

    public Leaf(char[] chars) {
        this.chars = chars;
    }

    public static Leaf of(char[] chars) {
        return new Leaf(chars);
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void addChars(char[] chars) {
        this.chars = chars;
    }

    public void removeChars() {
        this.chars = null;
    }

    public int countChars() {
        return this.chars.length;
    }

    public char[] getChars() {
        return this.chars.clone();
    }

    public StringBuilder compose(StringBuilder stringBuilder) {
        return stringBuilder.append(this.chars);
    }

    @Override
    public String toString() {
        return "Leaf{" +
                "type=" + type +
                ", chars=" + Arrays.toString(chars) +
                '}';
    }
}
