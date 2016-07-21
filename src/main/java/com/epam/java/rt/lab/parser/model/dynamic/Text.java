package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * parser
 */
public class Text extends BaseComposite implements Composite {
    private static final Logger LOGGER_TEXT = LoggerFactory.getLogger(Text.class);
    private Rule rule;

    public Text() {
        LOGGER_TEXT.info("Text constructed");
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public void setRule(Rule rule) {
        this.rule = rule;
        LOGGER_TEXT.info("Text rule set to '{}'", rule);
    }
}
