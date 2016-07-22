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
        tryParse(result);

    }

    private void tryParse(Result result) {
        /*
            a)  checking for startsWith-rule

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
                current rule        - created composite for lower level
                current composite   - created rule for lower level
                find index          - index from which starts search (find)
                found index         - index where requested pattern matches
         */
        LOGGER_PARSER.debug("tryParse: {}", result);
        boolean tryParseChild = true;
        // a)
        if (result.parentRule.getStartsWith() != null) {
            LOGGER_PARSER.debug("getStartsWith != null ({})", result.parentRule);
            tryParseChild = nextRuleComponent(result.parentRule.getStartsWith(), result);
        }
        if (tryParseChild) {
            // b)
            LOGGER_PARSER.debug("tryParseChild = true");
            Rule childRule;
            do {
                LOGGER_PARSER.debug("do-while");
                for (int i = 0; i < result.parentRule.countRules(); i++) {
                    childRule = result.parentRule.getRule(i);
                    LOGGER_PARSER.debug("childRule set to parentRule.getRule({}) = {}", i, childRule);
                    if (childRule.countRules() == 0) {
                        LOGGER_PARSER.debug("childRule.countRules() = 0");
                        nextRuleComponent(childRule, result);
                    } else {
                        Result childResult = new Result();
                        childResult.findIndex = result.findIndex;
                        childResult.parentRule = childRule;
                        childResult.parentComposite = new CompoundComponent(childRule);
                        LOGGER_PARSER.debug("childResult = ({})", childResult);
                        tryParse(childResult);
                        if (result.findIndex < childResult.findIndex) {
                            // c)
                            LOGGER_PARSER.debug("childResult positive (findIndex = {})", childResult.findIndex);
                            result.parentComposite.add((Component) childResult.parentComposite);
                            result.findIndex = childResult.findIndex;
                            result.foundIndex = childResult.foundIndex;
                        }
                    }
                }
            } while (result.findIndex < result.foundIndex);
        }
    }

    private boolean nextRuleComponent(Rule rule, Result result) {
        /*

         */
        Matcher matcher = rule.getPatternStart().matcher(this.source);
        if (matcher.find(result.findIndex)) {
            result.foundIndex = matcher.start();
            if (result.findIndex < result.foundIndex)
                LOGGER_PARSER.warn("Parser found some missing/un-parsable string ({})", this.source.substring(result.findIndex, result.foundIndex));
            result.findIndex = result.foundIndex;
            matcher = rule.getPatternEnd().matcher(this.source);
            if (matcher.find(result.findIndex)) {
                result.foundIndex = matcher.start();
                result.createdComponent = new ElementaryComponent(rule);
                result.createdComponent.addChars(this.source.substring(result.findIndex, result.foundIndex).toCharArray());
                result.parentComposite.add(result.createdComponent);
                LOGGER_PARSER.info("Component '{}' created and added to '{}'", result.createdComponent, result.parentComposite);
                return true;
            } else {
                throw new IllegalStateException("Parse error end-pattern not found (" +
                        this.source.substring(result.findIndex, result.findIndex + 15) + ")");
            }
        } else {
            result.foundIndex = -1;
        }
        return false;
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
                if (nextComponent.parentRule.getEndsWith().getPatternStart() != null) {
                    matcher = nextComponent.parentRule.getEndsWith().
                            getPatternStart().matcher(this.source);
                    if (matcher.find(nextComponent.findFrom)) {
                        nextComponent.tempStart = matcher.start();
                        nextComponent.tempRule = nextComponent.parentRule.getEndsWith();
                    }
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
