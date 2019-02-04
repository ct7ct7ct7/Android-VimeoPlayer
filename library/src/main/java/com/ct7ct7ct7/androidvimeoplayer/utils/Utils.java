package com.ct7ct7ct7.androidvimeoplayer.utils;

import android.annotation.SuppressLint;

public class Utils {
    public static String colorToHex(int color) {
        return String.format("%06X", (0xFFFFFF & color));
    }

    @SuppressLint("DefaultLocale")
    public static String formatTime(float sec) {
        int minutes = (int) (sec / 60);
        int seconds = (int) (sec % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
