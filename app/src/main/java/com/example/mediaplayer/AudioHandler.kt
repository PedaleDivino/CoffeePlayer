package com.example.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object AudioHandler {

    var musicPlayer = MediaPlayer()
    var trackName = ""
    var idTrack: Int ?= null

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

    fun skipForward(context: Context, path: Uri) {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicPlayer = MediaPlayer.create(context, path)
        musicPlayer.start()
    }

    fun skipBack(context: Context, path: Uri) {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicPlayer = MediaPlayer.create(context, path)
        musicPlayer.start()
    }

    fun pauseMusic() {
        if (musicPlayer.isPlaying) {
            musicPlayer.pause()
        }
    }

}