package com.epam.java.rt.lab.parser.model;

import java.util.*;

/**
 * parser
 */
public class Composite extends Component implements Iterable<Component> {
    private List<Component> components = new ArrayList<>();

    public Composite() {
    }

    public static Component of(Type type) {
        Component component = new Composite();
        component.setType(type);
        return component;
    }

    @Override
    public boolean addChildren(List<Component> components) {
        return this.components.addAll(components);
    }

    @Override
    public boolean addChild(Component component) {
        return this.components.add(component);
    }

    @Override
    public Component removeChild(int index) {
        return this.components.remove(index);
    }

    @Override
    public Component getChild(int index) {
        return this.components.get(index);
    }

    @Override
    public int countChildren() {
        return this.components.size();
    }

    public int countChildrenDeep() {
        int result = 0;
        for (Component component : this.components) {
            if (component instanceof Composite) {
                result += ((Composite) component).countChildrenDeep();
            } else {
                result += component.countChildren();
            }
        }
        return result;
    }

    @Override
    public StringBuilder compose(StringBuilder sb) {
        for (Component component : this.components)
            component.compose(sb);
        return sb;
    }

    @Override
    public String toString() {
        return "Composite{" +
                "type=" + this.getType().getName() +
                ", components=" + this.components.size() +
                '}';
    }

    @Override
    public Iterator<Component> iterator() {
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
                            if (((Composite) childComponent).components.size() > 0) {
                                if (!(((Composite) childComponent).components.get(0) instanceof Leaf)) {
                                    this.levels.put(this.levels.size(), ((Composite) childComponent).components);
                                }
                            }
                            if (childComponent.getType().getName().equals(type.getName())) {
                                this.currIndex = i;
                                this.currNext = true;
                                return true;
                            }
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