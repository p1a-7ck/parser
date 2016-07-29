package com.epam.java.rt.lab.parser.model;

/**
 * parser
 */
public class CharCache {
    private static final char[] chars = new char[65536];

    public static char[] cache(char[] chars) {
        int code;
        char ch;
        for (int i = 0; i < chars.length; i++) {
            code = Character.codePointAt(chars, i);
            ch = CharCache.chars[code];
            if (ch == CharCache.chars[0]) {
                ch = (char) code;
                CharCache.chars[code] = ch;
            }
            chars[i] = ch;
        }
        return chars;
    }

    public static String cachedCharsString() {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < CharCache.chars.length; i++)
            if (CharCache.chars[i] != CharCache.chars[0])
                st.append(CharCache.chars[i]);
        return st.toString();
    }

}
