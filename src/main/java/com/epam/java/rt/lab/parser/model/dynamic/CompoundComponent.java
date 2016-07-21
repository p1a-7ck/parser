package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * parser
 */
public class CompoundComponent extends BaseComposite implements Composite, Component {
    private static final Logger LOGGER_COMPOUND_COMPONENT = LoggerFactory.getLogger(CompoundComponent.class);
    private Rule rule;

    public CompoundComponent() {
        LOGGER_COMPOUND_COMPONENT.info("CompoundComponent constructed");
    }

    public CompoundComponent(Rule rule) {
        this.rule = rule;
        LOGGER_COMPOUND_COMPONENT.info("CompoundComponent constructed with rule set to '{}'", rule);
    }

    public static CompoundComponent of(Rule rule) {
        return new CompoundComponent(rule);
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public void setRule(Rule rule) {
        if (this.rule == null) {
            this.rule = rule;
            LOGGER_COMPOUND_COMPONENT.info("CompoundComponent rule set of '{}'", rule);
        } else {
            LOGGER_COMPOUND_COMPONENT.warn("CompoundComponent rule should be set only once");
        }
    }


    private SearchResult getComponentSymbol(int index) {
        SearchResult searchResult = new SearchResult();
        int compoundStartIndex = 0;
        for (int i = 0; i < super.count(); i++) {
            if (compoundStartIndex + super.get(i).countSymbols() >= index) {
                searchResult.component = super.get(i);
                searchResult.symbolIndexInComponent = index - compoundStartIndex;
                searchResult.symbol = searchResult.component.getSymbol(searchResult.symbolIndexInComponent);
                return searchResult;
            }
            compoundStartIndex += super.get(i).countSymbols();
        }
        return searchResult;
    }

    @Override
    public Symbol getSymbol(int index) {
        return getComponentSymbol(index).symbol;
    }

    @Override
    public char getChar(int index) {
        return getComponentSymbol(index).symbol.getC();
    }

    @Override
    public boolean addSymbol(Symbol symbol) {
        throw new IllegalStateException("Could not add symbol of compound component");
    }

    @Override
    public boolean addChar(char c) {
        throw new IllegalStateException("Could not add char of compound component");
    }

    @Override
    public boolean addChars(char[] c) {
        throw new IllegalStateException("Could not add chars of compound component");
    }

    @Override
    public Symbol removeSymbol(int index) {
        SearchResult searchResult = getComponentSymbol(index);
        return searchResult.component.removeSymbol(searchResult.symbolIndexInComponent);
    }

    @Override
    public int countSymbols() {
        int result = 0;
        for (int i = 0; i < super.count(); i++)
            result += super.get(i).countSymbols();
        return result;
    }

    @Override
    public String toString() {
        return "CompoundComponent{" +
                "rule=" + this.rule +
                ", components=" + super.count() +
                '}';
    }

    class SearchResult {
        Component component;
        Symbol symbol;
        int symbolIndexInComponent;
    }
}
