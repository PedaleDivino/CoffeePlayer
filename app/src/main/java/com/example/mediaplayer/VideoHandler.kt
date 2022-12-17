package com.example.mediaplayer

import android.net.Uri
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

object VideoHandler {

    var videoPlayer : ExoPlayer?= null
    var isPlaying = false
    var trackName = ""
    var idTrack: Int ?= null

    /*array = database
    for su database
    if i.id == id passato per parametro {save i. break}*/

    fun startPlaylistInPosition (lista: ArrayList<AudioAndVideo>,id: Int) {
        var saveID: Int = 0
        if (videoPlayer!!.mediaItemCount != 0) {
            videoPlayer!!.clearMediaItems()
        }
        for (i in 0 until lista.size) {
            videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse(lista[i].filePath)))
            if (lista[i].id == id) {
                saveID = i
            }
        }
        videoPlayer!!.seekToDefaultPosition(saveID)
    }

}