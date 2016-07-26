package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Composite implements Componentable {
    private final Type type;
    private final List<Componentable> componens;

    public Composite(Type type) {
        this.type = type;
        this.componens = new ArrayList<>();
    }

    public boolean add(Componentable component) {
        System.out.println(">>>" + component.compose(new StringBuilder()));
        return this.componens.add(component);
    }

    public Componentable remove(int index) {
        return this.componens.remove(index);
    }

    public Componentable get(int index) {
        return this.componens.get(index);
    }

    public int count() {
        return this.componens.size();
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public boolean addLeaf(Leaf leaf) {
        throw new IllegalStateException("This operation restricted for Composite");
    }

    @Override
    public boolean addLeafAll(List<Leaf> leafs) {
        throw new IllegalStateException("This operation restricted for Composite");
    }

    @Override
    public Leaf removeLeaf(int index) {
        throw new IllegalStateException("This operation restricted for Composite");
    }

    @Override
    public int countLeafs() {
        int result = 0;
        for (Componentable component : this.componens)
            result += component.countLeafs();
        return result;
    }

    @Override
    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Componentable component : this.componens) {
            component.compose(stringBuilder);
        }
        return stringBuilder;
    }

    @Override
    public String toString() {
        return "Composite{" +
                "type=" + type +
                '}';
    }
}