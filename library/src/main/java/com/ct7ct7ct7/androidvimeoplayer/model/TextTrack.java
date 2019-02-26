package com.ct7ct7ct7.androidvimeoplayer.model;

public class TextTrack {
    public String label;
    public String language;
    public String kind;
    public String mode;

    public TextTrack(String label, String language, String kind, String mode) {
        this.label = label;
        this.language = language;
        this.kind = kind;
        this.mode = mode;
    }
}
