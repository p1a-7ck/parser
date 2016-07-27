package com.epam.java.rt.lab.parser.model;

import java.util.*;

/**
 * parser
 */
public class Composite implements Componentable, Iterable<Componentable> {
    private final Type type;
    private final List<Componentable> components;

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



    @Override
    public Iterator iterator() {
        List<Componentable> list = new ArrayList<>();
        return getAll(this, list).iterator();
    }

    private List<Componentable> getAll(Componentable componentable, List<Componentable> list) {
        list.add(componentable);
        if (componentable instanceof Composite) {
            for (int i = 0; i < ((Composite) componentable).count(); i++)
                getAll(((Composite) componentable).get(i), list);
        }
        return list;
    }

    public Iterator<Componentable> iterator(Type type) {
        Iterator<Componentable> it = new Iterator<Componentable>() {
            Map<Integer, List<Componentable>> levels = new HashMap<>();
            List<Componentable> currLevel = null;
            int levelIndex = -1;
            int currIndex = -1;
            boolean nextIndex = false;

            @Override
            public boolean hasNext() {
                if (this.levels.size() == 0) {
                    this.levels.put(0, components);
                    this.levelIndex = 0;
                    this.currLevel = this.levels.get(this.levelIndex);
                }
                Componentable childComponentable;
                while(this.levelIndex < this.levels.size()) {
                    System.out.println(this.levelIndex + " < " + this.levels.size());
                    for (int i = this.currIndex + 1; i < this.currLevel.size(); i++) {
                        childComponentable = this.currLevel.get(i);
                        if (childComponentable instanceof Composite) {
                            this.levels.put(this.levels.size(), ((Composite) childComponentable).components);
                        }
                        if (childComponentable.getType().getName().equals(type.getName())) {
                            this.currIndex = i;
                            this.nextIndex = true;
                            return true;
                        }
                    }
                    this.levelIndex += 1;
                    this.currLevel = this.levels.get(this.levelIndex);
                }
                this.currIndex = -1;
                this.nextIndex = false;
                return false;
            }

            @Override
            public Componentable next() {
                if (!this.nextIndex) hasNext();
                this.nextIndex = false;
                if (this.currIndex < 0) return null;
                return this.currLevel.get(this.currIndex);
            }

        };
        return it;
    }

}