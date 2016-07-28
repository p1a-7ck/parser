package com.epam.java.rt.lab.parser.model;

import java.util.*;

/**
 * parser
 */
public class Composite implements Component, Iterable<Component> {
    private Type type;
    private List<Component> components = new ArrayList<>();

    public Composite() {
    }

    public Composite(Type type) {
        this.type = type;
    }

    public boolean add(Component component) {
        return this.components.add(component);
    }

    public Component remove(int index) {
        return this.components.remove(index);
    }

    public Component get(int index) {
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
    public void addChars(char[] chars) {
        throw new IllegalStateException("This operation restricted for Composite");
    }

    @Override
    public void removeChars() {
        throw new IllegalStateException("This operation restricted for Composite");
    }

    @Override
    public int countChars() {
        int result = 0;
        for (Component component : this.components)
            result += component.countChars();
        return result;
    }

    @Override
    public char[] getChars() {
        StringBuilder sb = new StringBuilder();
        for (Component component : this.components) {
            sb.append(component.getChars());
        }
        return sb.toString().toCharArray(); // bad retrieving char array
    }

    @Override
    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Component component : this.components) {
            component.compose(stringBuilder);
        }
        return stringBuilder;
    }

    @Override
    public String toString() {
        return "Composite{" +
                "type=" + this.type +
                ", components=" + this.components.size() +
                '}';
    }

    @Override
    public Iterator iterator() {
        return iterator(Type.of(""));
    }

    public Iterator<Component> iterator(Type type) {
        Iterator<Component> it = new Iterator<Component>() {
            Map<Integer, List<Component>> levels = new HashMap<>();
            List<Component> currLevel = null;
            int levelIndex = -1;
            int currIndex = -1;
            boolean currNext = false;

            private boolean getNextIndex() {
                Component childComponent;
                if ("".equals(type.getName())) {
                    for (int i = this.currIndex + 1; i < this.currLevel.size(); i++) {
                        childComponent = this.currLevel.get(i);
                        if (childComponent instanceof Composite) {
                            this.levels.put(this.levels.size(), ((Composite) childComponent).components);
                        }
                        this.currIndex = i;
                        this.currNext = true;
                        return true;
                    }
                } else {
                    for (int i = this.currIndex + 1; i < this.currLevel.size(); i++) {
                        childComponent = this.currLevel.get(i);
                        if (childComponent instanceof Composite) {
                            this.levels.put(this.levels.size(), ((Composite) childComponent).components);
                        }
                        if (childComponent.getType().getName().equals(type.getName())) {
                            this.currIndex = i;
                            this.currNext = true;
                            return true;
                        }
                    }
                }
                this.currIndex = -1;
                this.currNext = false;
                return false;
            }

            @Override
            public boolean hasNext() {
                if (this.levels.size() == 0) {
                    this.levels.put(0, components);
                    this.levelIndex = 0;
                    this.currLevel = this.levels.get(this.levelIndex);
                }
                while(this.levelIndex < this.levels.size()) {
                    if (getNextIndex()) return true;
                    this.levelIndex += 1;
                    this.currLevel = this.levels.get(this.levelIndex);
                }
                this.currIndex = -1;
                return false;
            }

            @Override
            public Component next() {
                if (!this.currNext) hasNext();
                this.currNext = false;
                if (this.currIndex < 0) throw new NoSuchElementException();
                return this.currLevel.get(this.currIndex);
            }

        };
        return it;
    }
}