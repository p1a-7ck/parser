package com.epam.java.rt.lab.parser.parser;

import com.epam.java.rt.lab.parser.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * parser
 */
public final class Ruler {
    private final static Logger logger = LoggerFactory.getLogger(Ruler.class);
    private final static int CONTAINS_KEY = 1, STARTS_WITH_KEY = 2, ENDS_WITH_KEY = 3, REGEX_START_KEY = 4, REGEX_END_KEY = 5;
    private final String[] init;
    private final Type rootType;
    private final Map<Type, Rule> rules;

    private Ruler(String[] init, Type rootType) {
        this.init = init;
        this.rootType = rootType;
        this.rules = new HashMap<>();
    }

    public static Ruler from(String fileName) {
        Properties properties = Ruler.readProperties(fileName);
        if (properties == null) return null;
        String root = properties.getProperty("root");
        if (root == null) return null;
        String[] init = root.split(",");
        if (init.length != 6) return null;
        Type rootType = new Type(properties.getProperty(init[0]));
        Ruler ruler = new Ruler(init, rootType);
        ruler.createTypes(properties);
        return ruler;
    }

    private static Properties readProperties(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = Ruler.class.getClassLoader().getResourceAsStream(fileName);
            Properties properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
            logger.info("Properties read from file '{}'", fileName);
            return properties;
        } catch (IOException exc) {
            logger.error("File '{}' not found or file read error", fileName, exc);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException exc) {
                logger.error("InputStream close error for file '{}'", fileName, exc);
            }
        }
        return null;
    }

    public Type getRootType() {
        return this.rootType;
    }

    public Pattern getTypeRulePatternStart(Type type) {
        Rule rule = this.rules.get(type);
        if (rule == null) return null;
        return rule.patternStart;
    }

    public Pattern getTypeRulePatternEnd(Type type) {
        Rule rule = this.rules.get(type);
        if (rule == null) return null;
        return rule.patternEnd;
    }

    public String toDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder = getTypeDetail(this.rootType, stringBuilder);
        return stringBuilder.toString();
    }

    private StringBuilder getTypeDetail(Type type, StringBuilder stringBuilder) {
        stringBuilder.append(type.getName()).append("{");
        stringBuilder.append(rules.get(type));
        for (int i = 0; i < type.countStartsWith(); i++) stringBuilder.append(", startsWith=").append(type.getStartsWith(i));
        for (int i = 0; i < type.countEndsWith(); i++) stringBuilder.append(", endsWith=").append(type.getEndsWith(i));
        if (type.countSubTypes() > 0) {
            stringBuilder.append(", subTypes=[");
            for (int i = 0; i < type.countSubTypes(); i++) {
                stringBuilder = getTypeDetail(type.getSubType(i), stringBuilder);
                if (i < type.countSubTypes()) stringBuilder.append(", ");
            }
            stringBuilder.append("]");
        }
        stringBuilder.append("}");
        return stringBuilder;
    }

    private void createTypes(Properties properties) {
        String typeContains = properties.getProperty(this.init[0].concat(this.init[CONTAINS_KEY]));
        if (typeContains == null)
            throw new IllegalStateException("Key .contains not found for " + this.rootType.getName());
        for (String subTypeSign : typeContains.split(","))
            createSubType(this.rootType, this.init[0].concat(".").concat(subTypeSign), properties);
    }

    private void createSubType(Type parentType, String typeSign, Properties properties) {
        logger.debug("Trying to create sub-type for '{}'", parentType.getName());
        String typeContains = properties.getProperty(typeSign.concat(this.init[CONTAINS_KEY]));
        if (typeContains == null) {
            Rule rule = getRule(typeSign, properties);
            if (rule == null)
                throw new IllegalStateException("Keys .startPattern or .endPattern not found for " + typeSign);
            Type type = new Type(properties.getProperty(typeSign));
            type.setParent(parentType);
            parentType.addSubType(type);
            this.rules.put(type, rule);
            logger.info("Sub-type '{}' created for '{}'", type.getName(), parentType.getName());
        } else {
            Type type = new Type(properties.getProperty(typeSign));
            type.setParent(parentType);
            parentType.addSubType(type);
            String typeSignStartsWith = properties.getProperty(typeSign.concat(this.init[STARTS_WITH_KEY]));
            if (typeSignStartsWith != null) {
                for (String subTypeSign : typeSignStartsWith.split(","))
                    type.addStartsWith(createServiceType(typeSign.concat(".").concat(subTypeSign), properties));
            }
            String typeSignEndsWith = properties.getProperty(typeSign.concat(this.init[ENDS_WITH_KEY]));
            if (typeSignEndsWith != null)
                for (String subTypeSign : typeSignEndsWith.split(","))
                    type.addEndsWith(createServiceType(typeSign.concat(".").concat(subTypeSign), properties));
            logger.info("Sub-type '{}' created for '{}'", type.getName(), parentType.getName());
            for (String subTypeSign : typeContains.split(","))
                createSubType(type, typeSign.concat(".").concat(subTypeSign), properties);
        }
    }

    private Type createServiceType(String typeSign, Properties properties) {
        Rule rule = getRule(typeSign, properties);
        if (rule == null)
            throw new IllegalStateException("Keys .startPattern or .endPattern not found for " + typeSign);
        Type type = new Type(properties.getProperty(typeSign));
        this.rules.put(type, rule);
        logger.info("Service type created '{}'", type.getName());
        return type;
    }

    private Rule getRule(String typeSign, Properties properties) {
        if (properties.getProperty(typeSign.concat(this.init[REGEX_START_KEY])) == null ||
                properties.getProperty(typeSign.concat(this.init[REGEX_END_KEY])) == null) return null;
        return new Rule(
                Pattern.compile(properties.getProperty(typeSign.concat(this.init[REGEX_START_KEY]))),
                Pattern.compile(properties.getProperty(typeSign.concat(this.init[REGEX_END_KEY])))
        );
    }

    private class Rule {
        Pattern patternStart;
        Pattern patternEnd;

        Rule(Pattern patternStart, Pattern patternEnd) {
            this.patternStart = patternStart;
            this.patternEnd = patternEnd;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "patternStart=" + patternStart +
                    ", patternEnd=" + patternEnd +
                    '}';
        }
    }
}