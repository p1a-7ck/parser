package com.epam.java.rt.lab.parser.service;

import com.epam.java.rt.lab.parser.model.Component;
import com.epam.java.rt.lab.parser.model.Composite;
import com.epam.java.rt.lab.parser.model.Type;

import java.util.*;

/**
 * parser
 */
public class WordAnalyzer {

    public static Map<String, Integer> wordsCount(Component component) {
        Map<String, Integer> result = new HashMap<>();
        Integer count;
        String word;
        Iterator it = ((Composite) component).iterator(Type.of("word"));
        while(it.hasNext()) {
            word = ((Component) it.next()).compose(new StringBuilder()).toString();
            count = result.get(word);
            if (count == null) count = 0;
            count += 1;
            result.put(word, count);
        }
        return result;
    }

    public static List<Map.Entry<String, Integer>> sortMapByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> result = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(result, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return result;
    }

    public static Map<Integer, List<String>> wordLength(Component component) {
        Map<Integer, List<String>> result = new HashMap<>();
        List<String> words;
        String word;
        Iterator it = ((Composite) component).iterator(Type.of("word"));
        while(it.hasNext()) {
            word = ((Component) it.next()).compose(new StringBuilder()).toString();
            words = result.get(word.length());
            if (words == null) {
                words = new ArrayList<>();
                result.put(word.length(), words);
            }
            if (!words.contains(word))
                words.add(word);
        }
        return result;
    }

    public static List<Map.Entry<Integer, List<String>>> sortMapByKey(Map<Integer, List<String>> map) {
        List<Map.Entry<Integer, List<String>>> result = new LinkedList<Map.Entry<Integer, List<String>>>(map.entrySet());
        Collections.sort(result, new Comparator<Map.Entry<Integer, List<String>>>() {
            @Override
            public int compare(Map.Entry<Integer, List<String>> o1, Map.Entry<Integer, List<String>> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return result;
    }

}
