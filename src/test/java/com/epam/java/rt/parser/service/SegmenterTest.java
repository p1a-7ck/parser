package com.epam.java.rt.parser.service;

import com.epam.java.rt.parser.model.RootSegment;
import org.junit.Test;

/**
 * parser
 */
public class SegmenterTest {

    @Test
    public void initialTest() {
        RootSegment rootSegment = new RootSegment(new StringBuilder("one,,,,        two,      ....  three   ..   four five six seven eight nine ten"));
        new ParseRuleReader(rootSegment,ParseRuleReader.getProperties("parse_rules.properties")).createParseRules();
//        new Segmenter(rootSegment).createParseSegments();
        new SegmenterO(rootSegment).parseSegments();




//        "sdsdijf hsdkfjs dkf sjdlkfj".split("\\s\\p{Punct}]*?")

//        System.out.println(rootSegment.getSegmentString(rootSegment.getSegment(0)));

    }

}