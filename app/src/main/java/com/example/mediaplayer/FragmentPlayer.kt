package com.example.mediaplayer

import android.animation.ObjectAnimator
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
import com.google.android.exoplayer2.offline.DownloadService.start
import kotlinx.android.synthetic.main.player_play.*


class FragmentPlayer : Fragment() {

    //Dichiarazzione delle variabili
    var disc: ImageView? = null
    var trackText : TextView ?= null
    var music: AudioHandler = AudioHandler
    var trackList: ArrayList<AudioAndVideo> = ArrayList()
    lateinit var thiscontext : Context
    var dbHandler : AudioAndVideoDatabaseHandler ?= null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Valorizzazione delle variabili
        var playerPlay : View = inflater.inflate(R.layout.player_play, container, false)
        thiscontext = container!!.context

        var play = playerPlay.findViewById(R.id.playButton) as ImageButton
        var pause = playerPlay.findViewById(R.id.pauseButtonId) as ImageButton
        var skipNext = playerPlay.findViewById(R.id.skipNextButton) as ImageButton
        var skipBack = playerPlay.findViewById(R.id.skipBackButton) as ImageButton
        var nameOfTrack = playerPlay.findViewById(R.id.trackNameId) as TextView

        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        trackList = dbHandler!!.readMP3Tracks()         //Funzione che ritorna una lista di soli file audio

        if (music.trackName != "") {            //Controlla se il nome della traccia contenuta in AudioHandler è vuota
            nameOfTrack.text = music.trackName          //Se non è vuota assegna il nome alla textView
        }
        else {
            nameOfTrack.text = "No track selected"          //Se è vuota gli assegna "No track selected" alla textView
        }

        if (music.musicPlayer.isPlaying) {            //Controlla se il MediaPlayer sta riproducendo
            //Se sta riproducendo fa sparire il bottone play e fa comparire il bottone di pause
            pause.visibility = View.VISIBLE
            play.visibility = View.GONE
        }
        else {
            //Se non sta riproducendo fa sparire il bottone pause e fa comparire il bottone di play
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
        }


        disc = playerPlay.findViewById(R.id.albumImageId)
        discAnimation()
        trackText = playerPlay.findViewById(R.id.trackNameId)
        musicTitleOnRepeat()


        play.setOnClickListener() {         //Alla pressione del tasto play
            if (music.idTrack != null) {            //Controlla se l'id della traccia da riprodurre non è nullo
                //Se non è nullo cambia la visibilità dei tasti e fa partire la musica
                play.visibility = View.GONE
                pause.visibility = View.VISIBLE
                music.startMusic()          //Funzione per riprodurre la musica
            }
            else {
                //Se è nullo avverte l'utente che è necessario selezionare un file audio
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        pause.setOnClickListener() {            //Alla pressione del tasto pause
            if (music.idTrack != null) {            //Controlla se l'id della traccia da riprodurre non è nullo
                //Se non è nullo cambia la visibilità dei tasti e mette in pausa la musica
                play.visibility = View.VISIBLE
                pause.visibility = View.GONE
                music.pauseMusic()          //Funzione per mettere in pausa la musica
            }
            else {
                //Se è nullo avverte l'utente che è necessario selezionare un file audio
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        skipNext.setOnClickListener(){          //Alla pressione del tasto skipNext
            if (music.idTrack == null) {            //Controlla se l'id della traccia da riprodurre non è nullo
                //Se è nullo avverte l'utente che è necessario selezionare un file audio
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
            else {
                //Log.d("ID TRACK prima" , music.idTrack.toString())
                //music.isPlaying = true
                if (trackList.size != 0) {          //Controlla se la lista di musica contiene almeno un elemento
                    //Cambia la visibilità dei tasti play e pause
                    play.visibility = View.GONE
                    pause.visibility = View.VISIBLE
                    var newPath =
                        getNextTrack(music.idTrack!!)         //Funzione per la ricerca della traccia successiva da riprodurre
                    if (newPath != null) {           //Controllo valorizzazione di newPath
                        //Se newPath è stata valorizzata cambierà la traccia in riproduzione al momento e il nome del brano
                        music.skipForward(thiscontext, Uri.parse(newPath))
                        nameOfTrack.text = music.trackName
                    } else {
                        //Se non è stato trovato il file successivo da riprodurre fa ripartire la musica dalla prima traccia inseita dall'utente
                        music.idTrack = trackList[0].id
                        music.trackName = trackList[0].fileName.toString()
                        newPath = trackList[0].filePath
                        music.createMusic(thiscontext, Uri.parse(newPath))
                        music.startMusic()
                        nameOfTrack.text = music.trackName
                    }
                } else {
                    music.pauseMusic()
                    pause.visibility = View.GONE
                    play.visibility = View.VISIBLE
                }
                //Log.d("ID TRACK dopo" , music.idTrack.toString())
            }
        }

        skipBack.setOnClickListener(){
            if (music.idTrack == null) {
                //Se è nullo avverte l'utente che è necessario selezionare un file audio
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
            else {
                //Log.d("ID TRACK prima" , music.idTrack.toString())
                //music.isPlaying = true

                if (trackList.size != 0) {          //Controlla se la lista di musica contiene almeno un elemento

                    //Cambia la visibilità dei tasti play e pause
                    play.visibility = View.GONE
                    pause.visibility = View.VISIBLE
                    var newPath =
                        getPreviousTrack(music.idTrack!!)         //Funzione per la ricerca della traccia precedente da riprodurre
                    if (newPath != null) {
                        //Se newPath è stata valorizzata cambierà la traccia in riproduzione al momento con quella nuova e il nome del brano
                        music.skipBack(thiscontext, Uri.parse(newPath))
                        nameOfTrack.text = music.trackName
                    } else {
                        //Se non è stato trovato il file precedente da riprodurre fa ripartire la musica dalla prima traccia inseita dall'utente
                        music.idTrack = trackList[0].id
                        music.trackName = trackList[0].fileName.toString()
                        newPath = trackList[0].filePath
                        music.createMusic(thiscontext, Uri.parse(newPath))
                        music.startMusic()
                        nameOfTrack.text = music.trackName
                    }
                } else {
                    music.pauseMusic()
                    pause.visibility = View.GONE
                    play.visibility = View.VISIBLE
                }
                //Log.d("ID TRACK dopo" , music.idTrack.toString())
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
        for (i in 0 until trackList.size) {         //ciclo per scorrere tutto lista di canzoni preso dal database
            if (trackList[i].id == id) {            //Ricerca della posizione all'interno della lista di canzoni tramite l'id della traccia
                if (i == trackList.size-1) {            //Controllo se la traccia è in ultima posizione all'interno della lista
                    //Se è l'ultimo elemento verrà ritornato il primo file nella lista
                    music.idTrack = trackList[0].id
                    music.trackName = trackList[0].fileName.toString()
                    path = trackList[0].filePath
                    return path
                }
                else {
                    //Se non è l'ultimo elemento ritorna l'elemento successivo della lista
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
        for (i in 0 until trackList.size) {         //ciclo per scorrere tutto lista di canzoni preso dal database
            if (trackList[i].id == id) {                  //Ricerca della posizione all'interno della lista di canzoni tramite l'id della traccia
                if (i == 0) {                             //Controllo se la traccia è in prima posizione all'interno della lista
                    //Se è il primo elemento verrà ritornato l'ultimo file nella lista
                    music.idTrack = trackList[trackList.size-1].id
                    music.trackName = trackList[trackList.size-1].fileName.toString()
                    path = trackList[trackList.size-1].filePath
                    return path
                }
                else {
                    //Se non è il primo elemento ritorna l'elemento precedente della lista
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

    fun musicTitleOnRepeat(){
        val repeatTitle = AnimationUtils.loadAnimation(activity,R.anim.repeat_title)
        repeatTitle.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        trackText?.animation = repeatTitle
    }


}