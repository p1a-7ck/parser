package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Type {
    private Type parent;
    private String name;
    private List<Type> startsWith;
    private List<Type> endsWith;
    private List<Type> types;

    public Type() {
        this.startsWith = new ArrayList<>();
        this.endsWith = new ArrayList<>();
        this.types = new ArrayList<>();
    }

    public Type(String name) {
        this.startsWith = new ArrayList<>();
        this.endsWith = new ArrayList<>();
        this.types = new ArrayList<>();
        this.name = name;
    }

    public static Type of(String name) {
        return new Type(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getParent() {
        return this.parent;
    }

    public void setParent(Type parent) {
        this.parent = parent;
    }

    public boolean addStartsWith(Type type) {
        return this.startsWith.add(type);
    }

    public Type removeStartsWith(int index) {
        return this.startsWith.remove(index);
    }

    public Type getStartsWith(int index) {
        return this.startsWith.get(index);
    }

    public int countStartsWith() {
        return this.startsWith.size();
    }

    public boolean addEndsWith(Type type) {
        return this.endsWith.add(type);
    }

    public Type removeEndsWith(int index) {
        return this.endsWith.remove(index);
    }

    public Type getEndsWith(int index) {
        return this.endsWith.get(index);
    }

    public int countEndsWith() {
        return this.endsWith.size();
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
