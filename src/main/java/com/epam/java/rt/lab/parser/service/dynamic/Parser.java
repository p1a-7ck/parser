package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Composite;
import com.epam.java.rt.lab.parser.model.dynamic.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * parser
 */
public class Parser {
    private final static Logger LOGGER_PARSER = LoggerFactory.getLogger(Parser.class);
    private Composite composite;
    private String source;

    public Parser() {
    }

    public static Parser to(Composite composite) {
        Parser parser = new Parser();
        parser.composite = composite;
        return parser;
    }

    public void parse(String source) {
        this.source = source;
        NextComponent nextComponent = new NextComponent();
        patternedRules(this.composite.getRule(), nextComponent);
        nextParse(nextComponent);
    }

    private void patternedRules(Rule parentRule, NextComponent nextComponent) {
        if (parentRule.countRules() > 0) {
            for (int i =0; i < parentRule.countRules(); i++)
                patternedRules(parentRule.getRule(i), nextComponent);
        } else {
            nextComponent.rules.add(parentRule);
        }
    }

    private void nextParse(NextComponent nextComponent) {
        
    }

    private void nextStartElementary(NextComponent nextComponent) {
        Matcher matcher;
        for (Rule rule : nextComponent.rules) {
            matcher = rule.getPatternStart().matcher(this.source);
            if (matcher.find(nextComponent.findFrom)) {
                if (nextComponent.tempStart < 0 || nextComponent.tempStart > matcher.start()) {
                    nextComponent.tempStart = matcher.start();
                    nextComponent.tempRule = rule;
                }
            }
        }
    }

    private void nextEndElementary(NextComponent nextComponent) {
        Matcher matcher = nextComponent.tempRule.getPatternEnd().matcher(this.source);
        if (matcher.find(nextComponent.findFrom)) {
            nextComponent.tempEnd = matcher.start();
            nextComponent.tempToResult();
        }
    }

    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    class NextComponent {
        List<Rule> rules = new ArrayList<>();
        Rule resultRule = null;
        int resultStart = -1;
        int resultEnd = -1;
        int findFrom = 0;

        Rule tempRule = null;
        int tempStart = -1;
        int tempEnd = -1;

        void reset() {
            resultRule = null;
            resultStart = -1;
            resultEnd = -1;
            tempRule = null;
            tempStart = -1;
            tempEnd = -1;
        }

        void tempToResult() {
            resultRule = tempRule;
            resultStart = tempStart;
            resultEnd = tempEnd;
        }
    }
}
