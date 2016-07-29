package com.epam.java.rt.lab.parser.model;

import java.util.List;

/**
 * parser
 */
public abstract class Component {
    private Type type;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean addChildren(List<Component> components) {
        throw new IllegalStateException();
    }

    public boolean addChild(Component component) {
        throw new IllegalStateException();
    }

    public Component removeChild(int index) {
        throw new IllegalStateException();
    }

    public Component getChild(int index) {
        throw new IllegalStateException();
    }

    public int countChildren() {
        throw new IllegalStateException();
    }

    public abstract StringBuilder compose(StringBuilder sb);
}
