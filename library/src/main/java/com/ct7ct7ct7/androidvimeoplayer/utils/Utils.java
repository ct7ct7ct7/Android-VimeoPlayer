package com.ct7ct7ct7.androidvimeoplayer.utils;

public class Utils {
    public static String colorToHex(int color) {
        return String.format("%06X", (0xFFFFFF & color));
    }
}
