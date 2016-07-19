package com.epam.java.rt.parser.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * parser
 */
public class Segment implements SegmentContainable {
    private final static Logger SEGMENT_LOGGER = LoggerFactory.getLogger(Segment.class);
    private final ParseRule parseRule;
    private final List<Segment> childSegments;
    private int start = -1;
    private int end = -1;

    public Segment(ParseRule parseRule) {
        this.parseRule = parseRule;
        this.childSegments = new ArrayList<>();
        SEGMENT_LOGGER.info("Segment constructed (parseRule = {})", parseRule);
    }

    public ParseRule getParseRule() {
        return this.parseRule;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        if (this.start < 0) this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        if (this.end < 0) this.end = end;
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
        return "Segment{" +
                "start='" + this.start + '\'' +
                ", end='" + this.end + '\'' +
                '}';
    }

}
