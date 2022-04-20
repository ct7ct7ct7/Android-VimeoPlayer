# Android-VimeoPlayer
Unofficial Vimeo video player library for Android.

*Inspired by [android-youtube-player](https://github.com/PierfrancescoSoffritti/android-youtube-player).*

[![](https://api.bintray.com/packages/ct7ct7ct7/maven/AndroidVimeoPlayer/images/download.svg)](https://bintray.com/ct7ct7ct7/maven/AndroidVimeoPlayer/_latestVersion)
[![website](https://img.shields.io/badge/-website-brightgreen.svg)](https://ct7ct7ct7.github.io/Android-VimeoPlayer/)

![screenshot](/screenshot.gif)

Gradle
------------


##### AndroidX
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.ct7ct7ct7.androidvimeoplayer:library:2.1'
}


android.useAndroidX=true
android.enableJetifier=true
```

##### Android Support Library
```groovy
dependencies {
    implementation 'com.ct7ct7ct7.androidvimeoplayer:library:1.2.2'
}
```

Usage
------------
```
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    
        <com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView
            android:id="@+id/vimeoPlayerView"
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
            app:aspectRatio="1.778"
            app:backgroundColor="#00FFFF"
            />
</LinearLayout>
```

```
lifecycle.addObserver(vimeoPlayerView)
vimeoPlayerView.initialize(true, 59777392)

// video background settings is OPEN and limit playing at embedded.
vimeoPlayerView.initialize(true, {YourPrivateVideoId}, "SettingsEmbeddedUrl")

//video background settings is PRIVATE.
vimeoPlayerView.initialize(true, {YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")


```

Properties
------------

* `app:topicColor` (default : 00adef) Specify the color of the video controls.
* `app:backgroundColor` (default : 000000) Specify the color of the video background.
* `app:autoPlay` (default : false) Automatically start playback of the video. 
* `app:loop` (default : false) Play the video again when it reaches the end.
* `app:muted` (default : false) Mute this video on load.
* `app:showTitle` (default : true) Show the title on the video.
* `app:showOriginalControls` (default : false) Show vimeo js original controls.
* `app:showFullscreenOption` (default : false) Show the fullscreen button on the native controls.
* `app:showMenuOption` (default : false) Show the menu button on the native controls.
* `app:aspectRatio` (default : 1.777..) Assign aspect ratio. default is 16/9.
* `app:quality` (default : "auto") Vimeo Plus, PRO, and Business members can set this config. Possible values:Auto, 4K, 2K, 1080p, 720p, 540p, 360p 



Menu options
------------
```
//show the menu option
vimeoPlayerView.setMenuVisibility(true)

//add item
vimeoPlayerView.addMenuItem(ViemoMenuItem("Item 1",R.drawable.icon, View.OnClickListener {
    //TODO
    vimeoPlayerView.dismissMenuItem()
}))

//override menu click listener
vimeoPlayerView.setMenuClickListener { 
    //TODO
}
```

Fullscreen options
------------
```
//show the fullscreen option
vimeoPlayerView.setFullscreenVisibility(true)



var REQUEST_CODE = 1234
.
.
.
//go to fullscreen page
vimeoPlayer.setFullscreenClickListener {

    //define the orientation
    var requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_AUTO
    
    startActivityForResult(VimeoPlayerActivity.createIntent(this, requestOrientation, vimeoPlayerView), REQUEST_CODE)
}
.
.
.
//handle return behavior
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
        var playAt = data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
        vimeoPlayerView.seekTo(playAt)

        var playerState = PlayerState.valueOf(data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE))
        when (playerState) {
            PlayerState.PLAYING -> vimeoPlayerView.play()
            PlayerState.PAUSED -> vimeoPlayerView.pause()
        }
    }
}

```


Methods
------------
```
val videoId: Int = vimeoPlayerView.videoId
val baseUrl: String? = vimeoPlayerView.baseUrl
val hashKey: String? = vimeoPlayerView.hashKey
val playerState: PlayerState = vimeoPlayerView.playerState
val currentTime: Float = vimeoPlayerView.currentTimeSeconds
vimeoPlayerView.setCaptions("en") //only supported original controls.
vimeoPlayerView.disableCaptions() //only supported original controls.
vimeoPlayerView.seekTo(0.0f)
vimeoPlayerView.loop = true
vimeoPlayerView.topicColor = Color.RED
vimeoPlayerView.setVolume(0.5f)
vimeoPlayerView.setPlaybackRate(0.5f)
vimeoPlayerView.play()
vimeoPlayerView.pause()
vimeoPlayerView.loadVideo(19231868)
vimeoPlayerView.recycle()
vimeoPlayerView.clearCache()
```


Listeners
------------
* `VimeoPlayerReadyListener`
* `VimeoPlayerStateListener`
* `VimeoPlayerTimeListener`
* `VimeoPlayerTextTrackListener`
* `VimeoPlayerVolumeListener`
* `VimeoPlayerErrorListener`

```
vimeoPlayerView.addReadyListener(object : VimeoPlayerReadyListener {
    override fun onReady(title: String?, duration: Float, textTrackArray: Array<TextTrack>) {
        //TODO
    }

    override fun onInitFailed() {
        //TODO
    }
})
```

```
vimeoPlayerView.addStateListener(object : VimeoPlayerStateListener {
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
vimeoPlayerView.addTimeListener { second: Float ->
    //TODO
}
```

```
vimeoPlayerView.addTextTrackListener { kind: String, label: String, language: String ->
    //TODO
}
```

```
vimeoPlayerView.addVolumeListener { volume: Float ->
    //TODO
}
```

```
vimeoPlayerView.addErrorListener { message: String, method: String, name: String ->
    //TODO
}
```
