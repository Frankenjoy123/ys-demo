package com.yunsoo.api.util;

import java.util.regex.Pattern;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/15
 * Descriptions:
 */
public class WildcardMatcher {

    /**
     * @param expression String with wildcard *, example: *, text*, *text, text*another
     * @param target     String
     * @return if is match
     */
    public static boolean match(String expression, String target) {
        if (expression == null || expression.length() == 0 || target == null) {
            return false;
        }
        char[] metaCharacters = {'^', '$', '[', ']', '(', ')', '{', '}', '|', '?', '*', '+', '-', '.', '\\'};
        StringBuilder regexSB = new StringBuilder("^");
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '*') {
                regexSB.append(".*");
            } else {
                for (char mc : metaCharacters) {
                    if (c == mc) {
                        regexSB.append('\\');
                        break;
                    }
                }
                regexSB.append(c);
            }
        }
        regexSB.append('$');
        return Pattern.compile(regexSB.toString()).matcher(target).matches();
    }
}
