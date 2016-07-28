package com.epam.java.rt.lab.parser;

import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.parser.Parser;
import com.epam.java.rt.lab.parser.parser.Ruler;
import com.epam.java.rt.lab.parser.service.WordAnalyzer;

import java.util.List;
import java.util.Map;

/**
 * parser
 */
public class Main {
    public static void main(String[] args) {

        Composite composite = Parser.with(Ruler.from("rules.properties")).parseFile("source.txt");
        Map<String, Integer> words = WordAnalyzer.wordsCount(composite);
        List<Map.Entry<String, Integer>> sorted = WordAnalyzer.sortedByValueMap(words);
        for (Map.Entry<String, Integer> item : sorted)
            System.out.println("'" + item.getKey() + "' = " + item.getValue() );

    }
}
