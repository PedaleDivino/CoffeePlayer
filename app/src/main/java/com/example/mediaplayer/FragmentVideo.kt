package com.example.mediaplayer

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class FragmentVideo : Fragment() {


    lateinit var thiscontext : Context
    var dbHandler : AudioAndVideoDatabaseHandler?= null
    var trackList: ArrayList<AudioAndVideo> = ArrayList()
    var player : ExoPlayer ?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var playerVideo : View = inflater.inflate(R.layout.player_video, container, false)

        thiscontext = container!!.context

        player = ExoPlayer.Builder(thiscontext).build()
        var videoPlayer = playerVideo.findViewById(R.id.video_player_view) as StyledPlayerView
        videoPlayer.player = player
        player!!.setMediaItem(MediaItem.fromUri(Uri.parse("/storage/emulated/0/Download/B.mp4")))
        player!!.prepare()
        player!!.play()
        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        trackList = dbHandler!!.readTracks()



        return playerVideo
    }

    companion object {
        fun newInstance() = FragmentVideo()
    }


}