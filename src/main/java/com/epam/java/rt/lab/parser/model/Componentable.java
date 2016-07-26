package com.epam.java.rt.lab.parser.model;

import java.util.List;

/**
 * parser
 */
public interface Componentable {
    Type getType();

    boolean addLeaf(Leaf leaf);

    boolean addLeafAll(List<Leaf> leafs);

    Leaf removeLeaf(int index);

    int countLeafs();

    StringBuilder compose(StringBuilder stringBuilder);
}
