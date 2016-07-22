package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        Result result = new Result();
        result.findIndex = 0;
        result.foundIndex = -1;
        result.parentComposite = this.composite;
        result.parentRule = this.composite.getRule();
        System.out.println(result.parentRule);
        tryParse(result);

    }

    private boolean tryParse(Result result) {
        /*
            a)  checking for startsWith-rule or endsWith

            b)  if startsWith-rule is null or found correctly, then trying to parse
                by childRules with setting current rule and current composite as a parent
                for children

            c)  if childRules return positive parse, then append current composite
                to parent composite, trying to parse next childRules, return positive
                to parent tryParse

            d)  if childRules return negative parse, then remove current composite
                and return negative to parent tryParse

            base fields for result:
                parent composite    - upper level composite
                parent rule         - upper level rule
                find index          - index from which starts search (find)
                found index         - index where requested pattern matches

                current rule        - created composite for lower level
                current composite   - created rule for lower level
         */
        LOGGER_PARSER.debug("tryParse: {}", result);
        boolean tryParseChild = true;
        boolean tryParseFound = false;
        // a)
        if (result.parentRule.getStartsWith() != null) {
            tryParseChild = nextRuleComponent(result.parentRule.getStartsWith(), result);
            if (tryParseChild) {
                result.createdComponent = new CompoundComponent(result.parentRule.getStartsWith());
                result.parentComposite.add(result.createdComponent);
            }
        }
        Rule childRule;
        boolean found;
        do {
            found = false;
            // a)
            if (result.parentRule.getEndsWith() != null) {
                tryParseChild = !nextRuleComponent(result.parentRule.getEndsWith(), result);
                if (!tryParseChild) {
                    result.createdComponent = new CompoundComponent(result.parentRule.getEndsWith());
                    result.parentComposite.add(result.createdComponent);
                    found = false;
                }
            }
            // b)
            if (tryParseChild) {
                for (int i = 0; i < result.parentRule.countRules(); i++) {
                    childRule = result.parentRule.getRule(i);
                    if (childRule.countRules() == 0) {
                        if (nextRuleComponent(childRule, result)) {
                            result.createdComponent = new CompoundComponent(childRule);
                            result.parentComposite.add(result.createdComponent);
                            tryParseFound = true;
                            found = true;
                        }
                    } else {
                        Result childResult = new Result();
                        childResult.findIndex = result.findIndex;
                        childResult.parentRule = childRule;
                        childResult.parentComposite = new CompoundComponent(childRule);
                        if (tryParse(childResult)) found = true;
                        if (result.findIndex < childResult.findIndex) {
                            // c)
                            result.parentComposite.add((Component) childResult.parentComposite);
                            result.findIndex = childResult.findIndex;
                            result.foundIndex = childResult.foundIndex;
                        }
                    }
                }
            }
        } while (found);
        // d)
        return tryParseFound;
    }

    private boolean nextRuleComponent(Rule rule, Result result) {
        Matcher matcher = rule.getPatternStart().matcher(this.source);
        if (matcher.find(result.findIndex)) {
            result.foundIndex = matcher.start();
            if (result.findIndex == result.foundIndex) {
                result.findIndex = result.foundIndex;
                matcher = rule.getPatternEnd().matcher(this.source);
                if (matcher.find(result.findIndex)) {
                    result.foundIndex = matcher.start();
                    result.createdComponent = new ElementaryComponent(rule);
                    result.createdComponent.addChars(this.source.substring(result.findIndex, result.foundIndex).toCharArray());
                    result.parentComposite.add(result.createdComponent);
                    result.findIndex = result.foundIndex;
                    return true;
                } else {
                    throw new IllegalStateException("Parse error end-pattern not found (" +
                            this.source.substring(result.findIndex, result.findIndex + 15) + ")");
                }
            } else {
                result.foundIndex = -1;
            }
        } else {
            result.foundIndex = -1;
        }
        return false;
    }

    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    class Result {
        int findIndex;
        int foundIndex;
        Composite parentComposite;
        Rule parentRule;

        Component createdComponent;

        @Override
        public String toString() {
            return "Result{" +
                    "findIndex=" + findIndex +
                    ", foundIndex=" + foundIndex +
                    ", parentComposite=" + parentComposite +
                    ", parentRule=" + parentRule +
                    '}';
        }
    }
}