package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Leaf {
    public final char ch;

    public Leaf(char ch) {
        this.ch = ch;
    }

    public static Leaf of(char ch) {
        return new Leaf(ch);
    }

    public static List<Leaf> from(char[] chs) {
        List<Leaf> leafs = new ArrayList<>();
        for (char ch : chs) leafs.add(Leaf.of(ch));
        return leafs;
    }

    public char getCh() {
        return this.ch;
    }

    @Override
    public String toString() {
        return String.valueOf(this.ch);
    }
}
