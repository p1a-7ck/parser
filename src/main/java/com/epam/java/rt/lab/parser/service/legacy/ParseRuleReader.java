package com.epam.java.rt.lab.parser.service.legacy;

import com.epam.java.rt.lab.parser.model.legacy.ParseRule;
import com.epam.java.rt.lab.parser.model.legacy.ParseRuleContainable;
import com.epam.java.rt.lab.parser.model.legacy.RootSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * parser
 */
public class ParseRuleReader {
    public final static Logger PARSE_RULE_READER_LOGGER = LoggerFactory.getLogger(ParseRuleReader.class);
    public final RootSegment rootSegment;
    public final Properties properties;
    public final String[] root;

    public ParseRuleReader(RootSegment rootSegment, Properties properties) {
        this.rootSegment = rootSegment;
        this.properties = properties;
        String property = properties.getProperty("root");
        if (property != null && property.length() > 0) {
            this.root = property.split(",");
            if (this.root.length != 4) {
                PARSE_RULE_READER_LOGGER.error("Property 'root' should have four elements separated by ','");
                throw new IllegalStateException();
            }
        } else {
            this.root = new String[]{};
            PARSE_RULE_READER_LOGGER.error("Property 'root' not found or empty");
            throw new IllegalStateException();
        }
        PARSE_RULE_READER_LOGGER.info("ParseRuleReader constructed ({}, {})",
                rootSegment, Arrays.toString(root));
    }

    public static Properties getProperties(String parseRuleFile) {
        InputStream file = ParseRuleReader.class.getClassLoader().getResourceAsStream(parseRuleFile);
        Properties properties = new Properties();
        try {
            properties.load(file);
            PARSE_RULE_READER_LOGGER.info("File loaded ({})", parseRuleFile);
            file.close();
        } catch (IOException exc) {
            PARSE_RULE_READER_LOGGER.error("File load error ({})", exc);
        }
        return properties;
    }

    public void createParseRules() {
        if (this.root.length == 4) {
            String property = this.properties.getProperty(this.root[0]);
            if (property != null) this.rootSegment.setBaseName(property);
            property = this.properties.getProperty(this.root[0] + this.root[1]);
            if (property != null && property.length() > 0) {
                for (String item : property.split(",")) {
                    createParseRule(rootSegment,
                            properties.getProperty(item),
                            properties.getProperty(item + root[1]),
                            properties.getProperty(item + root[2]),
                            properties.getProperty(item + root[3])
                    );
                }
            } else {
                PARSE_RULE_READER_LOGGER.error("Property 'all' not found");
                throw new IllegalStateException();
            }
        }
    }

    private void createParseRule(ParseRuleContainable parseRuleParent, String name, String ar, String st, String en) {
//        PARSE_RULE_READER_LOGGER.debug("name = {}, ar = {}, st = {}, en = {}", name, ar, st, en);
        ParseRule newParseRule = new ParseRule(parseRuleParent, name, Pattern.compile(st), Pattern.compile(en));
        parseRuleParent.addParseRule(newParseRule);
        if (ar != null && ar.length() > 0) {
            for (String item : ar.split(",")) {
                createParseRule(newParseRule,
                        properties.getProperty(item),
                        properties.getProperty(item + root[1]),
                        properties.getProperty(item + root[2]),
                        properties.getProperty(item + root[3])
                );
            }
        }
    }

}