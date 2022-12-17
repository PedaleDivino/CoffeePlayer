package com.example.mediaplayer

import android.content.Context
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.player_video.*
import kotlinx.android.synthetic.main.viewer.*
import kotlinx.android.synthetic.main.viewer.view.*

class FragmentVideo : Fragment() {


    lateinit var thiscontext : Context
    var dbHandler : AudioAndVideoDatabaseHandler?= null
    var video: VideoHandler = VideoHandler
    var aaaa: Prova = Prova



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var playerVideo : View = inflater.inflate(R.layout.player_video, container, false)

        thiscontext = container!!.context

        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        //viewer.bottom_navigation.visibility = BottomNavigationView.VISIBLE

        var play = playerVideo.findViewById(R.id.magheggioButton) as ImageButton

        play.setOnClickListener() {
            if (aaaa.provino!!.visibility == BottomNavigationView.GONE) {
                aaaa.provino!!.visibility = BottomNavigationView.VISIBLE
            }
            else {
                aaaa.provino!!.visibility = BottomNavigationView.GONE
            }
        }

        //video.videoPlayer = ExoPlayer.Builder(thiscontext).build()
        var videoPlayer = playerVideo.findViewById(R.id.video_player_view) as StyledPlayerView
        videoPlayer.player = video.videoPlayer

        if(video.videoPlayer!!.mediaItemCount == 0) {
            video.startPlaylistInPosition(dbHandler!!.readMP4Tracks(), 0)
        }

        /*video.videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse("/storage/emulated/0/Download/A.mp4"))) //0
        video.videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse("/storage/emulated/0/Download/A.mp4"))) //1
        video.videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse("/storage/emulated/0/Download/B.mp4"))) //2
        video.videoPlayer!!.addMediaItem(MediaItem.fromUri(Uri.parse("/storage/emulated/0/Download/A.mp4"))) //3
        video.videoPlayer!!.seekToDefaultPosition(2)*/
        video.videoPlayer!!.prepare()
        video.videoPlayer!!.play()
        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)




        return playerVideo
    }

    companion object {
        fun newInstance() = FragmentVideo()
    }


}