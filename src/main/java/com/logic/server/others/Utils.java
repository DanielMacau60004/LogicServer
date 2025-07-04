package com.logic.server.others;

public class Utils {

    public static String formatString(String str) {
        return str.replaceAll("^\"|\"$", "");
    }
}
