package com.epam.java.rt.lab.parser.model.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * parser
 */
public class Rule {
    private static final Logger LOGGER_RULE = LoggerFactory.getLogger(Rule.class);
    private String name;
    private Rule parent;
    private Rule startsWith;
    private Rule endsWith;
    private Pattern patternStart, patternEnd;
    private List<Rule> rules = new ArrayList<>();

    public Rule() {
        LOGGER_RULE.info("Rule constructed");
    }

    public Rule(String name) {
        this.name = name;
        LOGGER_RULE.info("Rule constructed with name set to '{}'", name);
    }

    public static Rule of(String name) {
        return new Rule(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (this.name == null) {
            this.name = name;
            LOGGER_RULE.info("Rule name set to '{}'", name);
        } else {
            LOGGER_RULE.warn("Rule name should be set only once");
        }
    }

    public Rule getParent() {
        return parent;
    }

    public void setParent(Rule parent) {
        if (this.parent == null) {
            this.parent = parent;
            LOGGER_RULE.info("Rule parent set to '{}'", parent);
        } else {
            LOGGER_RULE.warn("Rule parent should be set only once");
        }
    }

    public Rule getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(Rule startsWith) {
        if (this.startsWith == null) {
            this.startsWith = startsWith;
            LOGGER_RULE.info("Rule startsWith set to '{}'", startsWith);
        } else {
            LOGGER_RULE.warn("Rule startsWith should be set only once");
        }
    }

    public Rule getEndsWith() {
        return endsWith;
    }

    public void setEndsWith(Rule endsWith) {
        if (this.endsWith == null) {
            this.endsWith = endsWith;
            LOGGER_RULE.info("Rule endsWith set to '{}'", endsWith);
        } else {
            LOGGER_RULE.warn("Rule endsWith should be set only once");
        }
    }

    public Pattern getPatternStart() {
        return patternStart;
    }

    public void setPatternStart(Pattern patternStart) {
        if (this.patternStart == null) {
            this.patternStart = patternStart;
            LOGGER_RULE.info("Rule patternStart set to '{}'", this.patternStart.toString());
        } else {
            LOGGER_RULE.warn("Rule patternStart should be set only once");
        }
    }

    public Pattern getPatternEnd() {
        return patternEnd;
    }

    public void setPatternEnd(Pattern patternEnd) {
        if (this.patternEnd == null) {
            this.patternEnd = patternEnd;
            LOGGER_RULE.info("Rule patternEnd set to '{}'", this.patternEnd.toString());
        } else {
            LOGGER_RULE.warn("Rule patternEnd should be set only once");
        }
    }

    public Rule getRule(int index) {
        return this.rules.get(index);
    }

    public boolean addRule(Rule rule) {
        rule.setParent(this);
        LOGGER_RULE.info("Rule is adding rule '{}'", rule);
        return this.rules.add(rule);
    }

    public Rule removeRule(int index) {
        LOGGER_RULE.info("Rule is removing rule '{}'", index);
        return this.rules.remove(index);
    }

    public int countRules() {
        return this.rules.size();
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + this.name + '\'' +
                ", rules=" + this.rules.size() +
                '}';
    }

    public StringBuilder toDetails(StringBuilder sb) {
        sb.append("Rule{name='").append(this.name).append("' rules=[");
        for (int i = 0; i < this.rules.size(); i++) {
            this.rules.get(i).toDetails(sb);
        }
        sb.append("]}");
        return sb;
    }
}
