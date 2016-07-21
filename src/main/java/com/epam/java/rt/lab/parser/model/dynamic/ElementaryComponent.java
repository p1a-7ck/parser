package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class ElementaryComponent implements Component {
    private static final Logger LOGGER_ELEMENTARY_COMPONENT = LoggerFactory.getLogger(ElementaryComponent.class);
    private Rule rule;
    private List<Symbol> symbols = new ArrayList<>();

    public ElementaryComponent() {
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent constructed");
    }

    public ElementaryComponent(Rule rule) {
        this.rule = rule;
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent constructed with rule set to '{}'", rule);
    }

    public static ElementaryComponent of(Rule rule) {
        return new ElementaryComponent(rule);
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public void setRule(Rule rule) {
        if (this.rule == null) {
            this.rule = rule;
            LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent rule set of '{}'", rule);
        } else {
            LOGGER_ELEMENTARY_COMPONENT.warn("ElementaryComponent rule should be set only once");
        }
    }

    @Override
    public Symbol getSymbol(int index) {
        return this.symbols.get(index);
    }

    @Override
    public char getChar(int index) {
        return this.symbols.get(index).getC();
    }

    @Override
    public boolean addSymbol(Symbol symbol) {
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent is adding symbol '{}'", symbol);
        return this.symbols.add(symbol);
    }

    @Override
    public boolean addChar(char c) {
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent is adding char '{}'", c);
        return this.symbols.add(Symbol.of(c));
    }

    @Override
    public boolean addChars(char[] cArray) {
        boolean result = true;
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent is adding chars '{}'", cArray);
        for (char c : cArray)
            result &= this.symbols.add(Symbol.of(c));
        return result;
    }

    @Override
    public Symbol removeSymbol(int index) {
        LOGGER_ELEMENTARY_COMPONENT.info("ElementaryComponent is removing symbol at index '{}'", index);
        return this.symbols.remove(index);
    }

    @Override
    public int countSymbols() {
        return this.symbols.size();
    }

    @Override
    public StringBuilder compose(StringBuilder stringBuilder) {
        for (Symbol symbol : symbols)
            stringBuilder.append(symbol.getC());
        return stringBuilder;
    }

    @Override
    public String toString() {
        return "ElementaryComponent{" +
                "rule=" + this.rule +
                ", symbols=" + this.symbols.size() +
                '}';
    }
}
