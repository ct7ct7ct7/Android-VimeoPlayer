# Android-VimeoPlayer
Unofficial Vimeo video player library for Android.

[![](https://api.bintray.com/packages/ct7ct7ct7/maven/AndroidVimeoPlayer/images/download.svg)](https://bintray.com/ct7ct7ct7/maven/AndroidVimeoPlayer/_latestVersion)

![screenshot](/screenshot.gif)

### **Gradle**

```
dependencies {
    implementation 'com.ct7ct7ct7.androidvimeoplayer:library:1.0.1'
}
```

### **Usage**
```
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    
        <com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView
            android:id="@+id/vimeoPlayer"
            app:topicColor="#00FFFF"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
</LinearLayout>
```

```
VimeoPlayerView vimeoPlayer = findViewById(R.id.vimeoPlayer);
getLifecycle().addObserver(vimeoPlayer);
vimeoPlayer.initialize(59777392);

//If your video is pirvate.
//vimeoPlayer.initialize({YourPrivateVideoId}, "YourDomain")
```

### **Properties**
name               | default  | description
-------------------| -------- | -----------
app:autoPlay       | `false`  | Automatically start playback of the video. 
app:loop           | `false`  | Play the video again when it reaches the end.
app:muted          | `false`  | Mute this video on load.
app:showTitle      | `true`   | Show the title on the video.
app:playSinline    | `true`   | Play video inline on mobile devices, to automatically go fullscreen on playback set this parameter to `false`.
app:showPortrait   | `true`   | Show the portrait on the video.
app:showByline     | `true`   | Show the byline on the video.
app:showSpeed      | `false`  | Show the speed controls in the preferences menu and enable playback rate API (available to PRO and Business)
app:transparent    | `true`   | The responsive player and transparent background are enabled by default, to disable set this parameter to `false`.
app:topicColor     | `00adef` | Specify the color of the video controls. Colors may be overridden by the embed settings of the video.


### **Methods**
```
PlayerState playerState = vimeoPlayer.getPlayerState();
float currentTime = vimeoPlayer.getCurrentTimeSeconds();
vimeoPlayer.seekTo(0.0f);
vimeoPlayer.setLoop(true);
vimeoPlayer.setTopicColor(Color.RED);
vimeoPlayer.setVolume(0.5f);
vimeoPlayer.setPlaybackRate(0.5f);
```

### **Listeners**
* `VimeoPlayerStateListener`
* `VimeoPlayerTimeListener`
* `VimeoPlayerTextTrackListener`
* `VimeoPlayerVolumeListener`
* `VimeoPlayerErrorListener`

```
vimeoPlayer.addStateListener(new VimeoPlayerStateListener() {
    @Override
    public void onLoaded(int videoId) {
        //TODO
    }

    @Override
    public void onPlaying(float duration) {
        //TODO
    }

    @Override
    public void onPaused(float seconds) {
        //TODO
    }

    @Override
    public void onEnded(float duration) {
        //TODO
    }
    
    @Override
    public void onInitFailed() {
        //TODO
    }
});
```

```
vimeoPlayer.addTimeListener(new VimeoPlayerTimeListener() {
    @Override
    public void onCurrentSecond(float second) {
        //TODO
    }
});
```

```
vimeoPlayer.addTextTrackListener(new VimeoPlayerTextTrackListener() {
    @Override
    public void onTextTrackChanged(String kind, String label, String language) {
        //TODO
    }
});
```

```
vimeoPlayer.addVolumeListener(new VimeoPlayerVolumeListener() {
    @Override
    public void onVolumeChanged(float volume) {
        //TODO
    }
});
```

```
vimeoPlayer.addErrorListener(new VimeoPlayerErrorListener() {
    @Override
    public void onError(String message, String method, String name) {
        //TODO
    }
});
```
