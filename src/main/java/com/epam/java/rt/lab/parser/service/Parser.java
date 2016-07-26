package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.*;
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
    private final static Logger logger = LoggerFactory.getLogger(Parser.class);
    private final Composite composite;
    private final Ruler ruler;
    private String source;
    private int jumpOutRecursion = 0;

    public Parser(Ruler ruler) {
        this.ruler = ruler;
        this.composite = new Composite(ruler.getRootType());
    }

    public static Parser with(Ruler ruler) {
        Parser parser = new Parser(ruler);
        return parser;
    }

    public void parseFile(String fileName) {
        StringBuilder lines = new StringBuilder();
        try {
            InputStream in = Parser.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null)
                lines.append(line).append("\n");
            this.source = lines.toString();
        } catch (Exception exc) {
            logger.error("File '{}' not found or file read error", fileName, exc);
        }
        logger.info("Parsing from file '{}' initiated", fileName);
        int findFrom = 0;
//        while (findFrom < this.source.length())
            findFrom = findCompositeComponents(findFrom, this.composite);
    }

    private int findComponentStart(int findFrom, Type type) {
        Matcher matcher = ruler.getTypeRulePatternStart(type).matcher(this.source);
        if (!matcher.find(findFrom)) return -1;
        return matcher.start();
    }

    private Component getComponent(int componentStart, Type type) {
        Matcher matcher = ruler.getTypeRulePatternEnd(type).matcher(this.source);
        if (!matcher.find(componentStart)) {
            int componentEnd = componentStart + 15;
            if (componentEnd > this.source.length()) componentEnd = this.source.length();
            throw new IllegalStateException("PatternEnd not found for type '" + type.getName() + "'" +
                    " (..." + this.source.substring(componentStart, componentEnd) + "...)");
        }
        Component component = new Component(type);
        component.addLeafAll(Leaf.from(this.source.substring(componentStart, matcher.start()).toCharArray()));
        return component;
    }

    private void checkForParentsEndsWithComponent(int findFrom, Type type) {
        int jumpOutRecursion = 0;
        while (type.getParent() != null) {
            jumpOutRecursion += 1;
            type = type.getParent();
            if (type.getEndsWith() != null) {
                if (findFrom == findComponentStart(findFrom, type.getEndsWith())) {
                    this.jumpOutRecursion = jumpOutRecursion;
                    break;
                }
            }
        }
    }

    private int findCompositeStartsWithComponent(int findFrom, Composite composite) {
        if (composite.getType().startsWith == null) return findFrom;
        if (findFrom == findComponentStart(findFrom, composite.getType().startsWith)) {
            Component component = getComponent(findFrom, composite.getType().startsWith);
            composite.add(component);
            return findFrom + component.countLeafs();
        } else {
            return -1;
        }
    }

    private int findCompositeEndsWithComponent(int findFrom, Composite composite) {
        if (this.jumpOutRecursion == 0) checkForParentsEndsWithComponent(findFrom, composite.getType());
        if (this.jumpOutRecursion > 0) {
            this.jumpOutRecursion -= 1;
            return findFrom;
        }
        if (composite.getType().endsWith == null) return -1;
        if (findFrom == findComponentStart(findFrom, composite.getType().endsWith)) {
            Component component = getComponent(findFrom, composite.getType().endsWith);
            composite.add(component);
            return findFrom + component.countLeafs();
        } else {
            return -1;
        }
    }

    private int findCompositeComponents(int findFrom, Componentable composite) {
        logger.debug("findCompositeComponents ({}, {})", findFrom, composite);
        Type subType;
        Componentable childComposite;
        int findIndex = findCompositeStartsWithComponent(findFrom, (Composite) composite);
        if (findIndex == -1) return -1;
        do {
            findFrom = findIndex;
            findIndex = findCompositeEndsWithComponent(findFrom, (Composite) composite);
            if (findIndex >= 0) {
                findFrom = findIndex;
                break;
            }
            for (int i = 0; i < composite.getType().countSubTypes(); i++) {
                subType = composite.getType().getSubType(i);
                if (subType.countSubTypes() == 0) {
                    findIndex = findComponentStart(findFrom, subType);
                    if (findIndex == findFrom) {
                        Component component = getComponent(findFrom, subType);
                        ((Composite) composite).add(component);
                        findIndex = findFrom + component.countLeafs();
                        break;
                    }
                } else {
                    childComposite = new Composite(subType);
                    findIndex = findCompositeComponents(findFrom, childComposite);
                    if (findIndex > findFrom) {
                        ((Composite) composite).add(childComposite);
                        break;
                    }
                }
            }
            if (findFrom == 22) {
                findFrom = findFrom;
            }
        } while (findIndex > findFrom && this.jumpOutRecursion == 0);
        return findFrom;
    }

    public Composite getComposite() {
        return composite;
    }
}