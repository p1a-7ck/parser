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
    private final Ruler ruler;
    private String source;
    private int jumpOutRecursion = 0;

    public Parser(Ruler ruler) {
        this.ruler = ruler;
    }

    public static Parser with(Ruler ruler) {
        Parser parser = new Parser(ruler);
        return parser;
    }

    public Composite parseFile(String fileName) {
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
        Composite composite = new Composite(this.ruler.getRootType());
        int findFrom = 0;
//        while (findFrom < this.source.length() - 1)
            findFrom = findCompositeLeafs(findFrom, composite);
        return composite;
    }

    private int findTypeStart(int findFrom, Type type) {
        Matcher matcher = ruler.getTypeRulePatternStart(type).matcher(this.source);
        if (!matcher.find(findFrom)) return -1;
        return matcher.start();
    }

    private Leaf getTypeLeaf(int typeStart, Type type) {
        Matcher matcher = ruler.getTypeRulePatternEnd(type).matcher(this.source);
        if (!matcher.find(typeStart)) {
            int componentEnd = typeStart + 15;
            if (componentEnd > this.source.length()) componentEnd = this.source.length();
            throw new IllegalStateException("PatternEnd not found for type '" + type.getName() + "'" +
                    " (..." + this.source.substring(typeStart, componentEnd) + "...)");
        }
        Leaf leaf = Leaf.of(this.source.substring(typeStart, matcher.start()).toCharArray());
        leaf.setType(type);
        return leaf;
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
        }
    }

    private int findCompositeStartsWithType(int findFrom, Composite composite) {
        if (composite.getType().countStartsWith() == 0) return findFrom;
        for (int i = 0; i < composite.getType().countStartsWith(); i++) {
            if (findFrom == findTypeStart(findFrom, composite.getType().getStartsWith(i))) {
                Leaf leaf = getTypeLeaf(findFrom, composite.getType().getStartsWith(i));
                composite.add(leaf);
                return findFrom + leaf.countChars();
            }
        }
        return -1;
    }

    private int findCompositeEndsWithType(int findFrom, Composite composite) {
        if (this.jumpOutRecursion == 0) checkForParentsEndsWithType(findFrom, composite.getType());
        if (this.jumpOutRecursion > 0) {
            this.jumpOutRecursion -= 1;
            return findFrom;
        }
        if (composite.getType().countEndsWith() == 0) return -1;
        for (int i = 0; i < composite.getType().countEndsWith(); i++) {
            if (findFrom == findTypeStart(findFrom, composite.getType().getEndsWith(i))) {
                Leaf leaf = getTypeLeaf(findFrom, composite.getType().getEndsWith(i));
                composite.add(leaf);
                return findFrom + leaf.countChars();
            }
        }
        return -1;
    }

    private int findCompositeLeafs(int findFrom, Component composite) {
        logger.debug("findCompositeLeafs ({}, {})", findFrom, composite);
        Type subType;
        Component childComposite;
        int findIndex = findCompositeStartsWithType(findFrom, (Composite) composite);
        if (findIndex == -1) return -1;
        do {
            findFrom = findIndex;
            findIndex = findCompositeEndsWithType(findFrom, (Composite) composite);
            if (findIndex >= 0) {
                findFrom = findIndex;
                break;
            }
            for (int i = 0; i < composite.getType().countSubTypes(); i++) {
                subType = composite.getType().getSubType(i);
                if (subType.countSubTypes() == 0) {
                    findIndex = findTypeStart(findFrom, subType);
                    if (findIndex == findFrom) {
                        Leaf leaf = getTypeLeaf(findFrom, subType);
                        ((Composite) composite).add(leaf);
                        findIndex = findFrom + leaf.countChars();
                        break;
                    }
                } else {
                    childComposite = new Composite(subType);
                    findIndex = findCompositeLeafs(findFrom, childComposite);
                    if (findIndex > findFrom) {
                        ((Composite) composite).add(childComposite);
                        break;
                    }
                }
            }
        } while (findIndex > findFrom && this.jumpOutRecursion == 0);
        return findFrom;
    }
}