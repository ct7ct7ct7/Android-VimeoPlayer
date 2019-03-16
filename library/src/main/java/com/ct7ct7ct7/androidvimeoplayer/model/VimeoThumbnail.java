package com.ct7ct7ct7.androidvimeoplayer.model;

import com.google.gson.annotations.SerializedName;

public class VimeoThumbnail {

    public @SerializedName("thumbnail_url") String thumbnailUrl;

    public VimeoThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
