package com.example.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object AudioHandler {

    var musicPlayer = MediaPlayer()

    fun createMusic(context: Context, path: Uri) {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicPlayer = MediaPlayer.create(context, path)
    }

    fun startMusic() {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicPlayer.start()
    }

}