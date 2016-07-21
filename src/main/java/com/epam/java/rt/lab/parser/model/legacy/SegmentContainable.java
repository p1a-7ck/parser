package com.epam.java.rt.lab.parser.model.legacy;

/**
 * parser
 */
public interface SegmentContainable {
    int countSegments();
    Segment getSegment(int index);
    boolean addSegment(Segment segment);
}
