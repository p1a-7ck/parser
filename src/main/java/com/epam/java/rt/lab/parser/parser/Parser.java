package com.epam.java.rt.lab.parser.parser;

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
    private final Ruler ruler;
    private String source;
    private int jumpOutRecursion = 0;

    public Parser(Ruler ruler) {
        this.ruler = ruler;
    }

    public static Parser with(Ruler ruler) {
        return new Parser(ruler);
    }

    public Component parseFile(String fileName) {
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
        Component component = Composite.of(this.ruler.getRootType());
        findComponents(0, component);
//        int findFrom = 0;
//        while (findFrom < (this.source.length() - 1) && findFrom != -1)
//            findFrom = findComponents(findFrom, composite);
        return component;
    }

    private int findTypeStart(int findFrom, Type type) {
        Matcher matcher = ruler.getTypeRulePatternStart(type).matcher(this.source);
        if (!matcher.find(findFrom)) return -1;
        return matcher.start();
    }

    private Component getTypeComponent(int typeStart, Type type) {
        Matcher matcher = ruler.getTypeRulePatternEnd(type).matcher(this.source);
        if (!matcher.find(typeStart)) {
            int typeEndNotFound = typeStart + 15;
            if (typeEndNotFound > this.source.length()) typeEndNotFound = this.source.length();
            throw new IllegalStateException("PatternEnd not found for type '" + type.getName() + "'" +
                    " (..." + this.source.substring(typeStart, typeEndNotFound) + "...)");
        }
        Component component = Composite.of(type);
        component.addChildren(Leaf.of(CharCache.cache(this.source.substring(typeStart, matcher.start()).toCharArray())));
        return component;
    }

    private void checkForParentsEndsWithType(int findFrom, Type type) {
        int jumpOutRecursion = 0;
        while (type.getParent() != null) {
            jumpOutRecursion += 1;
            type = type.getParent();
            for (int i = 0; i < type.countEndsWith(); i++) {
                if (findFrom == findTypeStart(findFrom, type.getEndsWith(i))) {
                    this.jumpOutRecursion = jumpOutRecursion;
                    break;
                }
            }
            if (this.jumpOutRecursion > 0) break;
        }
    }

    private int findStartsWithTypeComponent(int findFrom, Component component) {
        if (component.getType().countStartsWith() == 0) return findFrom;
        for (int i = 0; i < component.getType().countStartsWith(); i++) {
            if (findFrom == findTypeStart(findFrom, component.getType().getStartsWith(i))) {
                Component childComponent = getTypeComponent(findFrom, component.getType().getStartsWith(i));
                component.addChild(childComponent);
                return findFrom + childComponent.countChildren();
            }
        }
        return -1;
    }

    private int findEndsWithTypeComponent(int findFrom, Component component) {
        if (component.getType().countEndsWith() == 0) return -1;
        for (int i = 0; i < component.getType().countEndsWith(); i++) {
            if (findFrom == findTypeStart(findFrom, component.getType().getEndsWith(i))) {
                Component childComponent = getTypeComponent(findFrom, component.getType().getEndsWith(i));
                component.addChild(childComponent);
                return findFrom + childComponent.countChildren();
            }
        }
        if (this.jumpOutRecursion == 0) checkForParentsEndsWithType(findFrom, component.getType());
        if (this.jumpOutRecursion > 0) {
            this.jumpOutRecursion -= 1;
            return findFrom;
        }
        return -1;
    }

    private int findComponents(int findFrom, Component component) {
        logger.debug("findComponents ({}, {})", findFrom, component);
        Type subType;
        Component childComponent;
        int findIndex = findStartsWithTypeComponent(findFrom, component);
        if (findIndex == -1) return -1;
        do {
            findFrom = findIndex;
            findIndex = findEndsWithTypeComponent(findFrom, component);
            if (findIndex >= 0 || this.jumpOutRecursion > 0) {
                if (findIndex >= 0) findFrom = findIndex;
                break;
            }
            for (int i = 0; i < component.getType().countSubTypes(); i++) {
                subType = component.getType().getSubType(i);
                if (subType.countSubTypes() == 0) {
                    findIndex = findTypeStart(findFrom, subType);
                    if (findIndex == findFrom) {
                        childComponent = getTypeComponent(findFrom, subType);
                        component.addChild(childComponent);
                        findIndex = findFrom + childComponent.countChildren();
                        break;
                    }
                    findIndex = -1;
                } else {
                    childComponent = Composite.of(subType);
                    findIndex = findComponents(findFrom, childComponent);
                    if (findIndex > findFrom) {
                        component.addChild(childComponent);
                        break;
                    }
                    findIndex = -1;
                }
            }
        } while (findIndex > findFrom && this.jumpOutRecursion == 0);
        return findFrom;
    }
}