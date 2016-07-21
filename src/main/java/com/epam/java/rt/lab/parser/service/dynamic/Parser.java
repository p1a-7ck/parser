package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Component;
import com.epam.java.rt.lab.parser.model.dynamic.Composite;
import com.epam.java.rt.lab.parser.model.dynamic.ElementaryComponent;
import com.epam.java.rt.lab.parser.model.dynamic.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void parseFile(String fileName) {
        StringBuilder lines = new StringBuilder();
        try {
            InputStream in = Parser.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            LOGGER_PARSER.info("Parser file '{}' found", fileName);
            while ((line = reader.readLine()) != null)
                lines.append(line).append("\n");
            parse(lines.toString());
        } catch (Exception exc) {
            LOGGER_PARSER.error("Parser not found file '{}' or file read error", fileName, exc);
        }
    }

    public void parse(String source) {
        this.source = source;
        NextComponent nextComponent = new NextComponent();
        nextComponent.parentRule = this.composite.getRule();
        nextComponent.composite = this.composite;
//        patternedRules(this.composite.getRule(), nextComponent);
//        while (nextComponent.findFrom < this.source.length())
            nextParse(nextComponent);
    }

//    private void patternedRules(Rule parentRule, NextComponent nextComponent) {
//        if (parentRule.countRules() > 0) {
//            for (int i =0; i < parentRule.countRules(); i++)
//                patternedRules(parentRule.getRule(i), nextComponent);
//        } else {
//            nextComponent.rules.add(parentRule);
//            if (parentRule.getEndsWith() != null)
//                nextComponent.ruleEndsWith = parentRule.getEndsWith();
//        }
//    }

    private void nextParse(NextComponent nextComponent) {
        // parse checking child rules patterns
        LOGGER_PARSER.debug("nextParse: {}", nextComponent);
        nextComponent.reset();
        nextStartElementary(nextComponent);
        if (nextComponent.tempStart == -1)
            LOGGER_PARSER.debug("nextStartElementary - found");
            if (nextComponent.tempStart > nextComponent.findFrom) {
                // there are some missing components
                Rule childRule;
                for (int i = 0; i < nextComponent.parentRule.countRules(); i++) {
                    childRule = nextComponent.parentRule.getRule(i);
                    NextComponent nextComponentChild =  new NextComponent();
                    nextComponentChild.findFrom = nextComponent.findFrom;
                    nextComponentChild.parentRule = childRule;
//                    nextComponentChild.composite = (Composite)
                    nextParse(nextComponentChild);
                    if (nextComponentChild.findFrom > nextComponent.findFrom)
                        break;
                }
            } else {
                // starts from findFrom
                nextEndElementary(nextComponent);
                if (nextComponent.tempEnd != -1) {
                    LOGGER_PARSER.debug("nextEndElementary - found");
                    // component found
                    Component component = new ElementaryComponent();
                    component.addChars(this.source.
                            substring(nextComponent.resultStart, nextComponent.resultEnd).toCharArray());
                    component.setRule(nextComponent.resultRule);
                    nextComponent.composite.add(component);
                }
            }
    }

    private void nextStartElementary(NextComponent nextComponent) {
        Rule childRule;
        Matcher matcher;
        if (!nextComponent.startsWithPass && nextComponent.parentRule.getStartsWith() != null) {
            matcher = nextComponent.parentRule.getStartsWith().
                    getPatternStart().matcher(this.source);
            if (matcher.find(nextComponent.findFrom)) {
                nextComponent.tempStart = matcher.start();
                nextComponent.tempRule = nextComponent.parentRule.getStartsWith();
            }
        } else {
            if (nextComponent.parentRule.getEndsWith() != null) {
                matcher = nextComponent.parentRule.getEndsWith().
                        getPatternStart().matcher(this.source);
                if (matcher.find(nextComponent.findFrom)) {
                    nextComponent.tempStart = matcher.start();
                    nextComponent.tempRule = nextComponent.parentRule.getEndsWith();
                }
            }
            if (nextComponent.tempStart > nextComponent.findFrom) {
                for (int i = 0; i < nextComponent.parentRule.countRules(); i++) {
                    childRule = nextComponent.parentRule.getRule(i);
                    if (childRule.getPatternStart() != null) {
                        matcher = childRule.getPatternStart().matcher(this.source);
                        if (matcher.find(nextComponent.findFrom)) {
                            if (nextComponent.tempStart < 0 || nextComponent.tempStart > matcher.start()) {
                                nextComponent.tempStart = matcher.start();
                                nextComponent.tempRule = childRule;
                            }
                        }
                    }
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
//        List<Rule> rules = new ArrayList<>();
        boolean startsWithPass = false;
        Rule parentRule = null;
        Rule resultRule = null;
        int resultStart = -1;
        int resultEnd = -1;
        int findFrom = 0;
        Composite composite;

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

        @Override
        public String toString() {
            return "NextComponent{" +
                    "startsWithPass=" + startsWithPass +
                    ", parentRule=" + parentRule +
                    ", resultRule=" + resultRule +
                    ", resultStart=" + resultStart +
                    ", resultEnd=" + resultEnd +
                    ", findFrom=" + findFrom +
                    ", composite=" + composite +
                    ", tempRule=" + tempRule +
                    ", tempStart=" + tempStart +
                    ", tempEnd=" + tempEnd +
                    '}';
        }
    }
}
