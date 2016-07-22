package com.epam.java.rt.lab.parser.service.dynamic;

import com.epam.java.rt.lab.parser.model.dynamic.Composite;
import com.epam.java.rt.lab.parser.model.dynamic.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * parser
 */
public class Ruler {
    private final static int CONTAINS_KEY = 1, STARTS_WITH_KEY = 2, ENDS_WITH_KEY = 3, REGEX_START_KEY = 4, REGEX_END_KEY = 5;
    private final static Logger LOGGER_RULER = LoggerFactory.getLogger(Ruler.class);
    private final Map<String, String> initRules = new HashMap<>();
    private String[] rootInitRule = null;
    private Composite composite;

    public Ruler() {
        LOGGER_RULER.info("Ruler constructed");
    }

    public static Ruler from(String fileName) {
        Ruler ruler = new Ruler();
        ruler.loadInitRulesData(fileName);
        return ruler;
    }

    public static Ruler of(String initRulesData) {
        Ruler ruler = new Ruler();
        ruler.setInitRulesData(Arrays.asList(initRulesData.split("([\\r]?+[\\n])|[\\r]")));
        return ruler;
    }

    public void setInitRulesData(List<String> initRulesData) {
        String[] keyValue;
        for (String initRule : initRulesData) {
            keyValue = initRule.split("=", 2);
            if (keyValue.length == 2) this.initRules.put(keyValue[0].trim(), keyValue[1].trim());
        }
        LOGGER_RULER.info("Ruler initRulesData mapped");
        if (this.initRules.containsKey("root")) {
            this.rootInitRule = this.initRules.get("root").split(",");
            if (this.rootInitRule.length == 6) {
                for (int i = 0; i < 6; i++)
                    this.rootInitRule[i] = this.rootInitRule[i].trim();
                LOGGER_RULER.info("initRulesData 'root' element found");
            } else {
                LOGGER_RULER.error("initRulesData 'root' element should define 6 peace of data: " +
                        "baseElementSign, containsKey, startsWithKey, endsWithKey, regexStartKey, regexEndKey");
            }
        } else {
            LOGGER_RULER.error("initRulesData have no 'root' element");
        }
    }

    public void loadInitRulesData(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            InputStream in = Ruler.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            LOGGER_RULER.info("Ruler file '{}' found", fileName);
            while ((line = reader.readLine()) != null)
                lines.add(line);
            setInitRulesData(lines);
        } catch (Exception x) {
            LOGGER_RULER.error("Ruler not found file '{}' or file read error", fileName);
        }
    }

    public void setRules(Composite composite) {
        if (this.rootInitRule != null && this.rootInitRule.length == 6) {
            this.composite = composite;
            this.composite.setRule(createRule(this.rootInitRule[0]));
        } else {
            LOGGER_RULER.error("Ruler does not correctly initialized");
        }
    }

    private Rule createRule(String ruleSign) {
        Rule rule = new Rule();
        rule.setName(this.initRules.get(ruleSign));
        if (this.initRules.containsKey(ruleSign.concat(this.rootInitRule[CONTAINS_KEY]))) {
            String ruleContains = this.initRules.get(ruleSign.concat(this.rootInitRule[CONTAINS_KEY]));
            if (ruleContains.length() > 0) {
                LOGGER_RULER.info("Ruler for '{}' rule 'CONTAINS_KEY' found", rule.getName());
                for (String item : ruleContains.split(","))
                    rule.addRule(createRule(ruleSign.concat(".").concat(item.trim())));
                if (this.initRules.containsKey(ruleSign.concat(this.rootInitRule[STARTS_WITH_KEY]))) {
                    String ruleStartsWith = this.initRules.get(ruleSign.concat(this.rootInitRule[STARTS_WITH_KEY]));
                    if (ruleStartsWith.length() > 0) {
                        LOGGER_RULER.info("Ruler for '{}' rule 'STARTS_WITH_KEY found", rule.getName());
                        rule.setStartsWith(createRule(ruleSign.concat(".").concat(ruleStartsWith)));
                    }
                }
                if (this.initRules.containsKey(ruleSign.concat(this.rootInitRule[ENDS_WITH_KEY]))) {
                    String ruleEndsWith = this.initRules.get(ruleSign.concat(this.rootInitRule[ENDS_WITH_KEY]));
                    if (ruleEndsWith.length() > 0) {
                        LOGGER_RULER.info("Ruler for '{}' rule 'ENDS_WITH_KEY found", rule.getName());
                        rule.setEndsWith(createRule(ruleSign.concat(".").concat(ruleEndsWith)));
                    }
                }
            } else {
                setRegex(ruleSign, rule);
            }
        } else {
            setRegex(ruleSign, rule);
        }
        return rule;
    }

    private void setRegex(String ruleSign, Rule rule) {
        if (this.initRules.containsKey(ruleSign.concat(this.rootInitRule[REGEX_START_KEY])))
            rule.setPatternStart(Pattern.compile(this.initRules.get(ruleSign.concat(this.rootInitRule[REGEX_START_KEY])).replace("\\\\","\\")));
        if (this.initRules.containsKey(ruleSign.concat(this.rootInitRule[REGEX_END_KEY])))
            rule.setPatternEnd(Pattern.compile(this.initRules.get(ruleSign.concat(this.rootInitRule[REGEX_END_KEY])).replace("\\\\","\\")));
    }
}
