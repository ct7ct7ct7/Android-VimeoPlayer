# Android-VimeoPlayer
Unofficial Vimeo video player library for Android.

[![](https://api.bintray.com/packages/ct7ct7ct7/maven/AndroidVimeoPlayer/images/download.svg)](https://bintray.com/ct7ct7ct7/maven/AndroidVimeoPlayer/_latestVersion)

![screenshot](/screenshot.gif)

### **Gradle**

```
dependencies {
    implementation 'com.ct7ct7ct7.androidvimeoplayer:library:1.0.6'
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
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:autoPlay="false"
            app:loop="false"
            app:muted="false"
            app:showFullscreenOption="false"
            app:showMenuOption="false"
            app:showOriginalControls="false"
            app:showTitle="true"
            app:topicColor="#FFFF00"
            />
</LinearLayout>
```

```
lifecycle.addObserver(vimeoPlayer)
vimeoPlayer.initialize(59777392)

//If video is open. but limit playing at embedded.
vimeoPlayer.initialize({YourPrivateVideoId}, "SettingsEmbeddedUrl")

//If video is pirvate.
vimeoPlayer.initialize({YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")


```

### **Properties**
name                    | default  | description
------------------------| -------- | -----------
app:topicColor          | `00adef` | Specify the color of the video controls.
app:autoPlay            | `false`  | Automatically start playback of the video. 
app:loop                | `false`  | Play the video again when it reaches the end.
app:muted               | `false`  | Mute this video on load.
app:showTitle           | `true`   | Show the title on the video.
app:showOriginalControls| `false`  | Show vimeo js original controls.
app:showFullscreenOption| `false`  | Show the fullscreen button on the native controls.
app:showMenuOption      | `false`  | Show the menu button on the native controls.


### **Menu options**
```
//show the menu option
vimeoPlayer.setMenuVisibility(true)

//add item
vimeoPlayer.addMenuItem(ViemoMenuItem("Item 1",R.drawable.icon, View.OnClickListener {
    //TODO
    vimeoPlayer.dismissMenuItem()
}))

//override menu click listener
vimeoPlayer.setMenuClickListener { 
    //TODO
}
```


### **Fullscreen options**
```
//show the fullscreen option
vimeoPlayer.setFullscreenVisibility(true)



var REQUEST_CODE = 1234
.
.
.
//go to fullscreen page
vimeoPlayer.setFullscreenClickListener {
    vimeoPlayer.pause()
    startActivityForResult(VimeoPlayerActivity.createIntent(this, vimeoPlayer), REQUEST_CODE)
}
.
.
.
//handle return behavior
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
        var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
        vimeoPlayer.seekTo(playAt)

        var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))
        when (playerState) {
            PlayerState.PLAYING -> vimeoPlayer.play()
            PlayerState.PAUSED -> vimeoPlayer.pause()
        }
    }
}

```

### **Methods**
```
val videoId: Int = vimeoPlayer.videoId
val baseUrl: String? = vimeoPlayer.baseUrl
val hashKey: String? = vimeoPlayer.hashKey
val playerState: PlayerState = vimeoPlayer.playerState
val currentTime: Float = vimeoPlayer.currentTimeSeconds
vimeoPlayer.seekTo(0.0f)
vimeoPlayer.loop = true
vimeoPlayer.topicColor = Color.RED
vimeoPlayer.setVolume(0.5f)
vimeoPlayer.setPlaybackRate(0.5f)
vimeoPlayer.play()
vimeoPlayer.pause()
vimeoPlayer.loadVideo(19231868)
```


### **Listeners**
* `VimeoPlayerReadyListener`
* `VimeoPlayerStateListener`
* `VimeoPlayerTimeListener`
* `VimeoPlayerTextTrackListener`
* `VimeoPlayerVolumeListener`
* `VimeoPlayerErrorListener`

```
vimeoPlayer.addReadyListener(object : VimeoPlayerReadyListener {
    override fun onReady(title: String?, duration: Float) {
        //TODO
    }

    override fun onInitFailed() {
        //TODO
    }
})
```

```
vimeoPlayer.addStateListener(object : VimeoPlayerStateListener {
    override fun onPlaying(duration: Float) {
        //TODO
    }

    override fun onPaused(seconds: Float) {
        //TODO
    }

    override fun onEnded(duration: Float) {
        //TODO
    }
})
```

```
vimeoPlayer.addTimeListener { second: Float ->
    //TODO
}
```

```
vimeoPlayer.addTextTrackListener { kind: String, label: String, language: String ->
    //TODO
}
```

```
vimeoPlayer.addVolumeListener { volume: Float ->
    //TODO
}
```

```
vimeoPlayer.addErrorListener { message: String, method: String, name: String ->
    //TODO
}
```
