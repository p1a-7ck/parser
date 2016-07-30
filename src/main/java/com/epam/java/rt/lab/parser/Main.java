package com.epam.java.rt.lab.parser;

import com.epam.java.rt.lab.parser.model.CharCache;
import com.epam.java.rt.lab.parser.model.Component;
import com.epam.java.rt.lab.parser.model.Leaf;
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

        Component component = Parser.with(Ruler.from("rules.properties")).parseFile("source.txt", true);
        Map<String, Integer> wordsCount = WordAnalyzer.wordsCount(component);
        List<Map.Entry<String, Integer>> sortedWordCount = WordAnalyzer.sortMapByValue(wordsCount);
        for (Map.Entry<String, Integer> item : sortedWordCount)
            System.out.println("'" + item.getKey() + "' = " + item.getValue());
        
        System.out.println(Leaf.toStringCached() + "\n");

        Map<Integer, List<String>> wordsLength = WordAnalyzer.wordLength(component);
        List<Map.Entry<Integer, List<String>>> sortedWordLength = WordAnalyzer.sortMapByKey(wordsLength);
        for (Map.Entry<Integer, List<String>> item : sortedWordLength)
            System.out.println(item.getKey() + " = " + item.getValue());

    }
}
