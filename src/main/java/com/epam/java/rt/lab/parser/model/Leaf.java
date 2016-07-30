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
