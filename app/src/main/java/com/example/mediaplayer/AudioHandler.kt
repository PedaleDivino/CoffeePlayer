package com.example.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

//  Una classe speciale di nome AudioHandler che può avere solo una istanza che gestisce il player degli MP3
object AudioHandler {

    var musicPlayer = MediaPlayer()         //  Variabile di tipo MediaPlayer che gestisce la riproduzione degli audio
    var trackName = ""                      //  Variabile di tipo Stringa che serve
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