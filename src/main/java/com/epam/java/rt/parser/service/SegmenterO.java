package com.epam.java.rt.parser.service;

import com.epam.java.rt.parser.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * parser
 */
public class SegmenterO {
    public final static Logger SEGMENTER_LOGGER = LoggerFactory.getLogger(SegmenterO.class);
    private RootSegment rootSegment;
    int findFrom = 0;
    boolean rr;

    public SegmenterO(RootSegment rootSegment) {
        this.rootSegment = rootSegment;
    }

    public void parseSegments() {
        NearestPattern nearestPattern = new NearestPattern();
        nearestPattern.segmentParent = this.rootSegment;
        nearestPattern.parseRuleParent = this.rootSegment;
        this.findFrom = 0;
        while (this.findFrom < this.rootSegment.getParsableData().length())
            parseSegment(nearestPattern);
    }

    private void parseSegment(NearestPattern nearestPattern) {
        findNearestStart(nearestPattern);
        SEGMENTER_LOGGER.debug("(!) index = {}", nearestPattern.index);
        if (nearestPattern.index >= 0) {
            Segment newSegment = new Segment(nearestPattern.parseRule);
            this.rootSegment.addSegment(newSegment);
            newSegment.setStart(nearestPattern.index);
            this.findFrom = nearestPattern.index;
            SEGMENTER_LOGGER.debug("parseRule = {}", newSegment.getParseRule().toString());
            if (nearestPattern.parseRule.countParseRules() > 0) {
                SEGMENTER_LOGGER.debug("count = {}", nearestPattern.parseRule.countParseRules());
                NearestPattern newNearestPattern = new NearestPattern();
                newNearestPattern.segmentParent = newSegment;
                newNearestPattern.parseRuleParent = nearestPattern.parseRule;
                while (findNearestEndOrStart(nearestPattern)) {
                    SEGMENTER_LOGGER.debug("WHILING");
                    parseSegment(newNearestPattern);
                }
            }
            findNearestEnd(nearestPattern);
            if (nearestPattern.index >= 0) {
                newSegment.setEnd(nearestPattern.index);
                this.findFrom = nearestPattern.index;
                nearestPattern.resetMatchValues();
            }
            SEGMENTER_LOGGER.debug("{} = '{}'", newSegment.getParseRule().getName(),
                    this.rootSegment.getSegmentString(newSegment));
        }
    }

    private boolean findNearestEndOrStart(NearestPattern nearestPattern) {
        SEGMENTER_LOGGER.debug("index = {}", nearestPattern.index);
        NearestPattern nearestPatternParent = nearestPattern.copyWithoutIndex();
        NearestPattern nearestPatternChild = nearestPattern.copyWithoutIndex();
        findNearestEnd(nearestPatternParent);
        findNearestStart(nearestPatternChild);
        SEGMENTER_LOGGER.debug("findFrom = {}, Pindex = {}, Cindex = {}", this.findFrom, nearestPatternParent.index, nearestPatternChild.index);
        if (nearestPatternParent.index >= this.findFrom) {
            SEGMENTER_LOGGER.debug("{}", (nearestPatternParent.index < nearestPatternChild.index || nearestPatternChild.index < this.findFrom));
            return (nearestPatternParent.index < nearestPatternChild.index || nearestPatternChild.index < this.findFrom);
        }
        SEGMENTER_LOGGER.warn("Started segment have no end");
        return false;
    }

    private void findNearestStartParent(NearestPattern nearestPattern) {
        if (nearestPattern.segmentParent instanceof Segment) {
            Matcher matcher = ((Segment) nearestPattern.segmentParent).getParseRule().
                    getStartPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(this.findFrom)) {
                nearestPattern.index = matcher.start();
                nearestPattern.parseRule = ((Segment) nearestPattern.segmentParent).getParseRule();
            }
        }
    }

    private void findNearestStart(NearestPattern nearestPattern) {
        Matcher matcher;
        SEGMENTER_LOGGER.debug("parseRuleParent = {}", nearestPattern.parseRuleParent.toString());
        for (int i = 0; i < nearestPattern.parseRuleParent.countParseRules(); i++) {
            matcher = nearestPattern.parseRuleParent.getParseRule(i).
                    getStartPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(this.findFrom)) {
                if (nearestPattern.index < this.findFrom || matcher.start() < nearestPattern.index) {
                    nearestPattern.index = matcher.start();
                    nearestPattern.parseRule = nearestPattern.parseRuleParent.getParseRule(i);
                }
            }
        }
    }

    private void findNearestEnd(NearestPattern nearestPattern) {
        Matcher matcher = nearestPattern.parseRule.getEndPattern().matcher(this.rootSegment.getParsableData());
        if (matcher.find(this.findFrom)) nearestPattern.index = matcher.start();
    }

    private class NearestPattern {
        SegmentContainable segmentParent;
        ParseRuleContainable parseRuleParent;
        ParseRule parseRule;
        int index = -1;

        NearestPattern copyWithoutIndex() {
            NearestPattern nearestPattern = new NearestPattern();
            nearestPattern.segmentParent = this.segmentParent;
            nearestPattern.parseRuleParent = this.parseRuleParent;
            nearestPattern.parseRule = this.parseRule;
            return nearestPattern;
        }

        void pasteMatchValues(NearestPattern nearestPattern) {
            this.parseRule = nearestPattern.parseRule;
            this.index = nearestPattern.index;
        }

        void resetMatchValues() {
            this.parseRule = null;
            this.index = -1;
        }

        @Override
        public String toString() {
            return "NearestPattern{" +
                    "segmentParent=" + segmentParent +
                    ", parseRuleParent=" + parseRuleParent +
                    ", parseRule=" + parseRule +
                    ", index=" + index +
                    '}';
        }
    }
}
