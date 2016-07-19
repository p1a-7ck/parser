package com.epam.java.rt.parser.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * parser
 */
public class RootSegment implements ParseRuleContainable, SegmentContainable {
    private final static Logger ROOT_SEGMENT_LOGGER = LoggerFactory.getLogger(RootSegment.class);
    private final StringBuilder parsableData;
    private String baseName;
    private final List<ParseRule> parseRules;
    private final List<Segment> childSegments;

    public RootSegment(StringBuilder parsableData) {
        this.parsableData = parsableData;
        this.parseRules = new ArrayList<>();
        this.childSegments = new ArrayList<>();
        ROOT_SEGMENT_LOGGER.info("RootSegment constructed parsableData length = {}", parsableData.length());
    }

    public String getSegmentString(Segment segment) {
        return parsableData.substring(segment.getStart(), segment.getEnd());
    }

    public StringBuilder getParsableData() {
        return parsableData;
    }

    public String getBaseName() {
        return this.baseName;
    }

    public void setBaseName(String baseName) {
        if (this.baseName == null) {
            this.baseName = baseName;
            ROOT_SEGMENT_LOGGER.info("RootSegment baseName set to '{}'", baseName);
        }
    }

    @Override
    public int countParseRules() {
        return this.parseRules.size();
    }

    @Override
    public ParseRule getParseRule(int index) {
        return this.parseRules.get(index);
    }

    @Override
    public boolean addParseRule(ParseRule parseRule) {
        return this.parseRules.add(parseRule);
    }

    @Override
    public int countSegments() {
        return this.childSegments.size();
    }

    @Override
    public Segment getSegment(int index) {
        return this.childSegments.get(index);
    }

    @Override
    public boolean addSegment(Segment segment) {
        return this.childSegments.add(segment);
    }

    @Override
    public String toString() {
        return "RootSegment{" +
                "'parsableData.length()'=" + this.parsableData.length() +
                '}';
    }
}
