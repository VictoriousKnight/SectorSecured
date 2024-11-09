package com.cybertooths.sectorsecured;
public class ConfigManager {
    private static final String a = "Q1RSTF80";
    private static final String b = "bHRfUHdu";
    public static String getDecodedKey() {
        String encodedKey = a + b;
        return new String(android.util.Base64.decode(encodedKey, android.util.Base64.DEFAULT));
    }
}