package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public abstract class BaseComposite implements Composite {
    private static final Logger LOGGER_BASE_COMPOSITE = LoggerFactory.getLogger(BaseComposite.class);
    private List<Component> components = new ArrayList<>();

    @Override
    public Component get(int index) {
        return this.components.get(index);
    }

    @Override
    public boolean add(Component component) {
        LOGGER_BASE_COMPOSITE.info("BaseComposite is adding component '{}'", component);
        return this.components.add(component);
    }

    @Override
    public Component remove(int index) {
        LOGGER_BASE_COMPOSITE.info("BaseComposite is removing component at index '{}'", index);
        return this.components.remove(index);
    }

    @Override
    public int count() {
        return this.components.size();
    }

    @Override
    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Component component : this.components)
            component.compose(stringBuilder);
        return stringBuilder;
    }

    @Override
    public String toString() {
        return "BaseComposite{" +
                ", components=" + components.size() +
                '}';
    }
}
