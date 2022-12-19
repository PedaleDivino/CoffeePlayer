package com.example.mediaplayer

import android.net.Uri
import com.example.mediaplayer.DB.AudioAndVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

object VideoHandler {

    var videoPlayer : ExoPlayer?= null          //  Variabile di tipo ExoPlayer per gestire video


    //  Funzione che riempie il videoPlayer di path ai video da far partire, e mette l'index della playlist alla posizione del file che ha ID uguale all'idFile passato per parametro
    fun startPlaylistInPosition (playlist: ArrayList<AudioAndVideo>, idFile: Int) {
        var saveID: Int = 0
        if (videoPlayer!!.mediaItemCount != 0) {
            videoPlayer!!.clearMediaItems()
        }
        for (i in 0 until playlist.size) {
            videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse(playlist[i].filePath)))
            if (playlist[i].id == idFile) {
                saveID = i
            }
        }
        videoPlayer!!.seekToDefaultPosition(saveID)
    }

    //  Funzione che rimuove dal videoPlayer un video che ha come id del file
    fun removeFileFromExoPlayer(playlist: ArrayList<AudioAndVideo>, idFile: Int) {
        var saveID: Int = 0
        for (i in 0 until playlist.size) {
            if (playlist[i].id == idFile) {
                saveID = i
                break
            }
        }

        videoPlayer!!.removeMediaItem(saveID)
    }

}