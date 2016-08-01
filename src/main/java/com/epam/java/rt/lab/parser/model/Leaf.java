package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * parser
 */
public class Leaf extends Component {
    private static final Map<Integer, Leaf> CACHE = new HashMap<>();
    private final char ch;

    public Leaf(char ch) {
        super(null);
        this.ch = ch;
    }

    public static Component of(char ch) {
        return new Leaf(ch);
    }

    public static List<Component> of(char[] chs) {
        List<Component> components = new ArrayList<>();
        for (char ch : chs) components.add(Leaf.of(ch));
        return components;
    }

    public static Component ofCached(char ch) {
        int code = (int) ch;
        Component component = Leaf.CACHE.get(code);
        if (component == null) {
            component = Leaf.of(ch);
            Leaf.CACHE.put(code, (Leaf) component);
        }
        return component;
    }

    public static List<Component> ofCached(char[] chs) {
        List<Component> components = new ArrayList<>();
        for (char ch : chs) components.add(Leaf.ofCached(ch));
        return components;
    }

    @Override
    public StringBuilder compose(StringBuilder sb) {
        return sb.append(this.ch);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leaf leaf = (Leaf) o;

        return ch == leaf.ch;

    }

    @Override
    public int hashCode() {
        return (int) ch;
    }

    @Override
    public String toString() {
        return "Leaf{" +
                "ch=" + ch +
                '}';
    }

    public static String toStringCached() {
        StringBuilder st = new StringBuilder();
        for (Map.Entry<Integer, Leaf> entry : Leaf.CACHE.entrySet())
            st.append(entry.getValue());
        return st.toString();
    }

}
