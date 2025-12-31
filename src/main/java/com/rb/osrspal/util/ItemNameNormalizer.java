package com.rb.osrspal.util;

public class ItemNameNormalizer {
    public static String normalize(String input) {
        return input.toLowerCase()
                .trim()
                .replace(" ", "_")
                .replace("'", "");
    }
}
