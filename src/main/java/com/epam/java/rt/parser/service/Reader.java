package com.epam.java.rt.parser.service;

import com.epam.java.rt.parser.model.Manager;
import com.epam.java.rt.parser.model.Part;
import com.epam.java.rt.parser.model.Rule;

/**
 * parser
 */
public class Reader {

    public void createRules(Manager manager, String rulesData) {
        // TODO read from XML-rulesData

        //
        manager.setRootRule(Rule.create(""));
        manager.getRootRule().addChild(Rule.create("\\w\\s"));
    }

    public void createParts(Manager manager, String partsData) {
        //

        //
        manager.setRootPart(Part.create("", manager.getRootRule()));
        manager.getRootPart().addChild(Part.create("Hello", manager.getRootRule().getChild(0)));
    }
}
