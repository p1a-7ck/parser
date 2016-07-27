package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Type {
    public final Type parent;
    public final String name;
    public final Type startsWith;
    public final Type endsWith;
    public final List<Type> types;

    public Type(Type parent, String name, Type startsWith, Type endsWith) {
        this.parent = parent;
        this.name = name;
        this.startsWith = startsWith;
        this.endsWith = endsWith;
        this.types = new ArrayList<>();
    }

    public static Type of(String name) {
        return new Type(null, name, null, null);
    }

    public String getName() {
        return this.name;
    }

    public Type getParent() {
        return this.parent;
    }

    public Type getStartsWith() {
        return this.startsWith;
    }

    public Type getEndsWith() {
        return this.endsWith;
    }

    public boolean addSubType(Type type) {
        return this.types.add(type);
    }

    public Type removeSubType(int index) {
        return this.types.remove(index);
    }

    public Type getSubType(int index) {
        return this.types.get(index);
    }

    public int countSubTypes() {
        return this.types.size();
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                '}';
    }
}
