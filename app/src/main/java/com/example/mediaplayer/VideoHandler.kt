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