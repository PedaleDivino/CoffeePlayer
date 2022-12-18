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

    //Inizializzazione delle variabili
    lateinit var thiscontext : Context
    var dbHandler : AudioAndVideoDatabaseHandler?= null
    var video: VideoHandler = VideoHandler
    var aaaa: Prova = Prova



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Valorizzazione delle variabili
        var playerVideo : View = inflater.inflate(R.layout.player_video, container, false)

        thiscontext = container!!.context

        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        //viewer.bottom_navigation.visibility = BottomNavigationView.VISIBLE

        var play = playerVideo.findViewById(R.id.magheggioButton) as ImageButton

        play.setOnClickListener() {         //Alla pressione del tasto play
            if (aaaa.provino!!.visibility == BottomNavigationView.GONE) {           //Controllo se la navBar è nascosta
                //Se è invisibile viene messa come visibile
                aaaa.provino!!.visibility = BottomNavigationView.VISIBLE
            }
            else {
                //Se è visibile viene messa come invisibile
                aaaa.provino!!.visibility = BottomNavigationView.GONE
            }
        }

        //Associazione dell'ExoPlayer alla StyledPlayerView
        var videoPlayer = playerVideo.findViewById(R.id.video_player_view) as StyledPlayerView
        videoPlayer.player = video.videoPlayer

        if(video.videoPlayer!!.mediaItemCount == 0) {           //Controllo se la playlist interna all'ExoPlayer è vuota
            //Se vuota richiama la funzione per l'avvio dei video passando come parametri la lista di tutti i video e l'id del primo file video
            video.startPlaylistInPosition(dbHandler!!.readMP4Tracks(), 0)
        }

        //Avvio dell'ExoPlayer
        video.videoPlayer!!.prepare()
        video.videoPlayer!!.play()
        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)




        return playerVideo
    }

    companion object {
        fun newInstance() = FragmentVideo()
    }


}