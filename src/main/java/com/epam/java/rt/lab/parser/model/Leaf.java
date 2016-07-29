package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Leaf extends Component {
    private char ch;

    public Leaf() {
    }

    public static Component of(char ch) {
        Component component = new Leaf();
        ((Leaf) component).ch = ch;
        return component;
    }

    public static List<Component> of(char[] chs) {
        List<Component> components = new ArrayList<>();
        for (char ch : chs) components.add(Leaf.of(ch));
        return components;
    }

    @Override
    public StringBuilder compose(StringBuilder sb) {
        return sb.append(this.ch);
    }

    @Override
    public String toString() {
        return "Leaf{" +
                "ch=" + ch +
                '}';
    }
}
