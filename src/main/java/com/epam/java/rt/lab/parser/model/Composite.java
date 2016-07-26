package com.epam.java.rt.lab.parser.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * parser
 */
public class Composite implements Componentable {
    private final Type type;
    private final List<Componentable> components;
    private List<Componentable> list;

    public Composite(Type type) {
        this.type = type;
        this.components = new ArrayList<>();
    }

    public boolean add(Componentable component) {
        return this.components.add(component);
    }

    public Componentable remove(int index) {
        return this.components.remove(index);
    }

    public Componentable get(int index) {
        return this.components.get(index);
    }

    public int count() {
        return this.components.size();
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
        for (Componentable component : this.components)
            result += component.countLeafs();
        return result;
    }

    @Override
    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Componentable component : this.components) {
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

    public List<Componentable> componentsList(String typeName) {
        this.list = new ArrayList<>();
        listAdd(this, typeName);
        return this.list;
    }

    private void listAdd(Componentable componentable, String typeName) {
        if (componentable.getType().getName().equals(typeName)) {
            this.list.add(componentable);
        }
        if (componentable instanceof Composite) {
            for (int i = 0; i < ((Composite) componentable).count(); i++)
                listAdd(((Composite) componentable).get(i), typeName);
        }
    }

}