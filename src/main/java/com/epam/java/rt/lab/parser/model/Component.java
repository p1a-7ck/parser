package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Component implements Componentable {
    private final Type type;
    private final List<Leaf> leafs;

    public Component(Type type) {
        this.type = type;
        this.leafs = new ArrayList<>();
    }

    public Type getType() {
        return this.type;
    }

    public boolean addLeaf(Leaf leaf) {
        return this.leafs.add(leaf);
    }

    public boolean addLeafAll(List<Leaf> leafs) {
        return this.leafs.addAll(leafs);
    }

    public Leaf removeLeaf(int index) {
        return this.leafs.remove(index);
    }

    public int countLeafs() {
        return this.leafs.size();
    }

    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Leaf leaf : this.leafs) {
            stringBuilder.append(leaf.toString());
        }
        return stringBuilder;
    }

    @Override
    public String toString() {
        return "Component{" +
                "type=" + type +
                ", leafs=" + this.compose(new StringBuilder()).toString() +
                '}';
    }
}
