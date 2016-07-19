package com.epam.java.rt.parser.model;

/**
 * parser
 */
public interface SegmentContainable {
    int countSegments();
    Segment getSegment(int index);
    boolean addSegment(Segment segment);
}
