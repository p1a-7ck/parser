package com.epam.java.rt.parser.service;

import com.epam.java.rt.parser.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * parser
 */
public class Segmenter {
    public final static Logger SEGMENTER_LOGGER = LoggerFactory.getLogger(Segmenter.class);
    private RootSegment rootSegment;
    private int currentPos;
    private ParseRule parseRuleCurrent;
    private int nearestStart;
    private int nearestEnd;
    private ParseRule parseRuleStartNearest;
    private ParseRule parseRuleEndNearest;

    public Segmenter(RootSegment rootSegment) {
        this.rootSegment = rootSegment;
    }

    public void createParseSegments() {


        ValueSegment valueSegment = new ValueSegment();

        valueSegment.segmentParent = this.rootSegment;
        valueSegment.parseRuleParent = this.rootSegment;
        valueSegment.findFrom = 0;
        while (valueSegment.findFrom < this.rootSegment.getParsableData().length()) {
            SEGMENTER_LOGGER.debug("valueSegment.findFrom = {}", valueSegment.findFrom);
            getNextSegment(valueSegment);

        }


    }

    private void getNextSegment(ValueSegment valueSegment) {
        NearestPattern(valueSegment);
        if (valueSegment.nextStart >= 0) {
            Segment newSegment = new Segment(valueSegment.nextParseRule);
            newSegment.setStart(valueSegment.nextStart);
            this.rootSegment.addSegment(newSegment);
            if (valueSegment.nextParseRule.countParseRules() > 0) {
                ValueSegment newValueSegment = new ValueSegment();
                newValueSegment.segmentParent = newSegment;
                newValueSegment.parseRuleParent = newSegment.getParseRule();
                newValueSegment.findFrom = valueSegment.findFrom;
                getNextSegment(newValueSegment);
            }
            valueSegment.findFrom = valueSegment.nextStart;
            NearestPattern(valueSegment);
            if (valueSegment.nextEnd >= 0) {
                newSegment.setEnd(valueSegment.nextEnd);
                valueSegment.findFrom = valueSegment.nextEnd;
                valueSegment.reset();
                SEGMENTER_LOGGER.debug("{} = '{}'",
                        newSegment.getParseRule().getName(), this.rootSegment.getSegmentString(newSegment));
            }
            rootSegment.getSegmentString(newSegment);
        }
    }

    private void NearestPattern(ValueSegment valueSegment) {
        Matcher matcher;
        if (valueSegment.segmentParent instanceof Segment) {
            matcher = ((Segment) valueSegment.segmentParent).getParseRule().
                    getStartPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(valueSegment.findFrom)) valueSegment.currentStart = matcher.start();
        }
        if (valueSegment.nextStart < 0) {
            for (int i = 0; i < valueSegment.parseRuleParent.countParseRules(); i++) {
                matcher = valueSegment.parseRuleParent.getParseRule(i).
                        getStartPattern().matcher(this.rootSegment.getParsableData());
                if (matcher.find(valueSegment.findFrom) &&
                        (valueSegment.nextStart < valueSegment.findFrom || matcher.start() < valueSegment.nextStart)) {
                    valueSegment.nextStart = matcher.start();
                    valueSegment.nextParseRule = valueSegment.parseRuleParent.getParseRule(i);
                }
            }
            SEGMENTER_LOGGER.debug(".nextStart = {}", valueSegment.nextStart);
        } else {
            if (valueSegment.nextParseRule != null) {
                matcher = valueSegment.nextParseRule.getEndPattern().matcher(this.rootSegment.getParsableData());
                if (matcher.find(valueSegment.findFrom)) valueSegment.nextEnd = matcher.start();
            }
            SEGMENTER_LOGGER.debug(".nextStart = {}, .nextEnd = {}", valueSegment.nextStart, valueSegment.nextEnd);
        }
    }

    private void getNearestStartPattern(ValueSegment valueSegment) {
        Matcher matcher;
        if (valueSegment.segmentParent instanceof Segment) {
            matcher = ((Segment) valueSegment.segmentParent).getParseRule().
                    getStartPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(valueSegment.findFrom)) valueSegment.currentStart = matcher.start();
        }
        ParseRule childParseRule;
        for (int i = 0; i < valueSegment.parseRuleParent.countParseRules(); i++) {
            childParseRule = valueSegment.parseRuleParent.getParseRule(i);
            matcher = childParseRule.getStartPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(valueSegment.findFrom)) {
//                SEGMENTER_LOGGER.debug("matcher.index() = {} / {}", matcher.index(), childParseRule);
                if (valueSegment.nextStart < valueSegment.findFrom || matcher.start() < valueSegment.nextStart) {
//                    SEGMENTER_LOGGER.debug("valueSegment changed to '{}'", matcher.index());
                    valueSegment.nextStart = matcher.start();
//                    valueSegment.nextParseRuleStart = childParseRule;
                }
            }
        }
    }

    private void getNearestEndPattern(ValueSegment valueSegment) {
        Matcher matcher;
        if (valueSegment.segmentParent instanceof Segment) {
            matcher = ((Segment) valueSegment.segmentParent).getParseRule().
                    getEndPattern().matcher(this.rootSegment.getParsableData());
            if (matcher.find(valueSegment.findFrom)) valueSegment.currentEnd = matcher.start();
        }
//        matcher = valueSegment.nextParseRuleStart.getEndPattern().matcher(this.rootSegment.getParsableData());
//        if (matcher.find(valueSegment.findFrom)) {
//            valueSegment.nextEnd = matcher.index();
//            valueSegment.nextParseRuleEnd = valueSegment.nextParseRuleStart;
//        }
//        ParseRule childParseRule;
//        for (int i = 0; i < valueSegment.parseRuleParent.countParseRules(); i++) {
//            childParseRule = valueSegment.parseRuleParent.getParseRule(i);
//            matcher = childParseRule.getEndPattern().matcher(this.rootSegment.getParsableData());
//            if (matcher.find(valueSegment.findFrom)) {
//                if (valueSegment.nextEnd < valueSegment.findFrom || matcher.index() < valueSegment.nextEnd) {
//                    valueSegment.nextEnd = matcher.index();
//                    valueSegment.nextParseRuleEnd = childParseRule;
//                }
//            }
//        }
    }

    public RootSegment getRootSegment() {
        return rootSegment;
    }

    public void setRootSegment(RootSegment rootSegment) {
        this.rootSegment = rootSegment;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public ParseRule getParseRuleCurrent() {
        return parseRuleCurrent;
    }

    public void setParseRuleCurrent(ParseRule parseRuleCurrent) {
        this.parseRuleCurrent = parseRuleCurrent;
    }

    public int getNearestStart() {
        return nearestStart;
    }

    public void setNearestStart(int nearestStart) {
        this.nearestStart = nearestStart;
    }

    public int getNearestEnd() {
        return nearestEnd;
    }

    public void setNearestEnd(int nearestEnd) {
        this.nearestEnd = nearestEnd;
    }

    public ParseRule getParseRuleStartNearest() {
        return parseRuleStartNearest;
    }

    public void setParseRuleStartNearest(ParseRule parseRuleStartNearest) {
        this.parseRuleStartNearest = parseRuleStartNearest;
    }

    public ParseRule getParseRuleEndNearest() {
        return parseRuleEndNearest;
    }

    public void setParseRuleEndNearest(ParseRule parseRuleEndNearest) {
        this.parseRuleEndNearest = parseRuleEndNearest;
    }

    private class ValueSegment {
        int findFrom = 0;
        ParseRuleContainable parseRuleParent;
        int currentStart = -1;
        int currentEnd = -1;
        ParseRule nextParseRule;
        int nextStart = -1;
        int nextEnd = -1;

        SegmentContainable segmentParent;

        void reset() {
            this.nextStart = -1;
            this.nextEnd = -1;
            this.nextParseRule = null;
        }
    }
}
