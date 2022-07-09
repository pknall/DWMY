package com.ccgautomation.utilities;

import java.util.List;

public class StringTools {

    public static String removeDoubleQuotesFromStrings(String text) {
        return text.replace("\"", "");
    }

    public static String convertStringListToString(List<String>list) {
        StringBuilder sb = new StringBuilder();
        if (list == null) return null;
        if (list.size() < 0) return null;

        for (String s : list) {
            sb.append(s);
            sb.append("\r\n");
        }

        return sb.toString();
    }
}
