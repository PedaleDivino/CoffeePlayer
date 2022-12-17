package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler


class FragmentPlayer : Fragment() {

    var disc: ImageView? = null
    var music: AudioHandler = AudioHandler
    var trackList: ArrayList<AudioAndVideo> = ArrayList()
    lateinit var thiscontext : Context
    var dbHandler : AudioAndVideoDatabaseHandler ?= null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var playerPlay : View = inflater.inflate(R.layout.player_play, container, false)
        thiscontext = container!!.context

        var play = playerPlay.findViewById(R.id.playButton) as ImageButton
        var pause = playerPlay.findViewById(R.id.pauseButtonId) as ImageButton
        var skipNext = playerPlay.findViewById(R.id.skipNextButton) as ImageButton
        var skipBack = playerPlay.findViewById(R.id.skipBackButton) as ImageButton
        var nameOfTrack = playerPlay.findViewById(R.id.trackNameId) as TextView

        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        trackList = dbHandler!!.readMP3Tracks()

        if (music.trackName != "") {
            nameOfTrack.text = music.trackName
        }
        else {
            nameOfTrack.text = "No track selected"
        }

        if (music.isPlaying) {
            pause.visibility = View.VISIBLE
            play.visibility = View.GONE
        }
        else {
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
        }


        disc = playerPlay.findViewById(R.id.albumImageId)
        discAnimation()

        play.setOnClickListener() {
            if (music.idTrack != null) {
                play.visibility = View.GONE
                pause.visibility = View.VISIBLE
                music.startMusic()
            }
            else {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        pause.setOnClickListener() {
            if (music.idTrack != null) {
                play.visibility = View.VISIBLE
                pause.visibility = View.GONE
                music.pauseMusic()
            }
            else {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        skipNext.setOnClickListener(){
            if (music.idTrack == null) {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
            else {
                Log.d("ID TRACK prima" , music.idTrack.toString())
                music.isPlaying = true
                play.visibility = View.GONE
                pause.visibility = View.VISIBLE
                var newPath = getNextTrack(music.idTrack!!)
                if (newPath != null){
                    music.skipForward(thiscontext, Uri.parse(newPath))
                    nameOfTrack.text = music.trackName
                }
                else {
                    music.idTrack = trackList[0].id
                    music.trackName = trackList[0].fileName.toString()
                    newPath = trackList[0].filePath
                    music.createMusic(thiscontext, Uri.parse(newPath))
                    music.startMusic()
                    nameOfTrack.text = music.trackName
                }
                Log.d("ID TRACK dopo" , music.idTrack.toString())
            }
        }

        skipBack.setOnClickListener(){
            if (music.idTrack == null) {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
            else {
                Log.d("ID TRACK prima" , music.idTrack.toString())
                music.isPlaying = true
                play.visibility = View.GONE
                pause.visibility = View.VISIBLE
                var newPath = getPreviousTrack(music.idTrack!!)
                if (newPath != null){
                    music.skipBack(thiscontext, Uri.parse(newPath))
                    nameOfTrack.text = music.trackName
                }
                else {
                    music.idTrack = trackList[0].id
                    music.trackName = trackList[0].fileName.toString()
                    newPath = trackList[0].filePath
                    music.createMusic(thiscontext, Uri.parse(newPath))
                    music.startMusic()
                    nameOfTrack.text = music.trackName
                }
                Log.d("ID TRACK dopo" , music.idTrack.toString())
            }
        }

        return playerPlay

    }

    companion object {
        fun newInstance() = FragmentPlayer()
    }

    //  Prendi la prossima track della playlist (l'ultima track fa ricominciare la playlist)
    fun getNextTrack(id: Int): String? {
        var path: String ?= null
        for (i in 0 until trackList.size) {
            if (trackList[i].id == id) {
                if (i == trackList.size-1) {
                    music.idTrack = trackList[0].id
                    music.trackName = trackList[0].fileName.toString()
                    path = trackList[0].filePath
                    return path
                }
                else {
                    music.idTrack = trackList[i+1].id
                    music.trackName = trackList[i+1].fileName.toString()
                    path = trackList[i+1].filePath
                    return path
                }
            }
        }
        return path
    }


    //  Prendi la track precedente della playlist (l'ultima track fa ricominciare la playlist)
    fun getPreviousTrack(id: Int): String? {
        var path: String ?= null
        for (i in 0 until trackList.size) {
            if (trackList[i].id == id) {
                if (i == 0) {
                    music.idTrack = trackList[trackList.size-1].id
                    music.trackName = trackList[trackList.size-1].fileName.toString()
                    path = trackList[trackList.size-1].filePath
                    return path
                }
                else {
                    music.idTrack = trackList[i-1].id
                    music.trackName = trackList[i-1].fileName.toString()
                    path = trackList[i-1].filePath
                    return path
                }
            }
        }
        return path
    }




    fun discAnimation() {

        val rotate = AnimationUtils.loadAnimation(activity,R.anim.rotation_disc)
        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val continueRotate = AnimationUtils.loadAnimation(activity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }

}