package com.example.mediaplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.player_video.*

class FragmentVideo : Fragment() {


    lateinit var thiscontext : Context
    lateinit var videoController : android.widget.MediaController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var playerVideo : View = inflater.inflate(R.layout.player_video, container, false)
        thiscontext = container!!.context

        playerVideo.findViewById(R.id.videoView) as VideoView
        videoController = MediaController(thiscontext)
        videoController.setAnchorView(videoView)

        return playerVideo
    }

    companion object {
        fun newInstance() = FragmentVideo()
    }


}