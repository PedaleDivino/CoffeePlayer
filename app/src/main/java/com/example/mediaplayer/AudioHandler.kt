package com.example.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object AudioHandler {

    var musicPlayer = MediaPlayer()
    var isPlaying = false  //  SE PO ANCHE LEVARE
    var trackName = ""
    var idTrack: Int ?= null

    fun createMusic(context: Context, path: Uri) {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
            isPlaying = false
        }
        musicPlayer = MediaPlayer.create(context, path)
    }

    fun startMusic() {
        if (musicPlayer.isPlaying) {
            musicPlayer.stop()
        }
        musicPlayer.start()
        isPlaying = true
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
            isPlaying = false
        }
    }

}