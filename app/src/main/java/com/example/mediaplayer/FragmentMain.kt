package com.example.mediaplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class FragmentMain() : Fragment() {

    //Dichiarazzione delle variabili e valorizzazione
    var miniPlayer: RelativeLayout? = null
    var dbHandler: AudioAndVideoDatabaseHandler? = null
    private var adapter: AudioAndVideoAdapter? = null
    private var fileList: ArrayList<AudioAndVideo>? = null
    private var fileListItem: ArrayList<AudioAndVideo>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var thiscontext: Context
    var music: AudioHandler = AudioHandler
    var supportVars: Support = Support
    var video: VideoHandler = VideoHandler


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var activityMain: View = inflater.inflate(R.layout.activity_main, container, false)

        setHasOptionsMenu(true)

        //  Valorizzazione delle variabili
        thiscontext = container!!.context
        fileListItem = ArrayList<AudioAndVideo>()
        adapter = AudioAndVideoAdapter(fileListItem!!, thiscontext,this)            //INIZIALIZZO LA VARIAILE ADAPTER INSERENDO IL VALORE DELLA CLASSE ToDoListApadter (CHE HA BISOGNO DI ARGOMENTI)
        fileList = ArrayList<AudioAndVideo>()
        layoutManager = LinearLayoutManager(thiscontext)                                    //INIZIALIZZO LAYOUTMANAGER CON UN MANAGER DI TIPO VERTICALE (HA BISOGNO DEL CONTESTO)

        activityMain.recyclerViewId.layoutManager = layoutManager                           //ASSOCIO IL LAYOUT MANAGER DA ME CREATO CON IL RECYCLEVIEWID DELL'ACTIVITY
        activityMain.recyclerViewId.adapter = adapter                                       //ASSOCIO L'ADAPTER DA ME CREATO CON QUELLO DEL RECYCLEVIEWID DELL'ACTIVITY
        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        miniPlayer = activityMain.findViewById(R.id.miniplayer)


        //  Riempio fileList in base a quale tipo di visualizzazione è stata selezionata
        when (supportVars.typeDisplay) {
            0 -> {
                fileList = dbHandler!!.readTracks()
            }
            1 -> {
                fileList = dbHandler!!.readMP3Tracks()
            }
            2 -> {
                fileList = dbHandler!!.readMP4Tracks()
            }
            else -> {

            }
        }

        //  Inverto la playlist e la faccio stampare
        fileList!!.reverse()
        for (t in fileList!!.iterator()) {
            val file = AudioAndVideo()
            file.fileName = t.fileName
            file.id = t.id
            file.filePath = t.filePath
            file.fileType = t.fileType

            fileListItem!!.add(file)
        }

        //  Se viene cliccato il bottone di ID "aaa" dell'activityMain richiamo una funzione che fa selezionare un file da aggiungere all'app
        val addTracksVideos: View = activityMain.findViewById(R.id.aaa)
        addTracksVideos.setOnClickListener() {
            requestRuntimePermission()
        }

        val miniPlayerButton : View = activityMain.findViewById(R.id.aac)
        val miniPlayerDownButton : View = activityMain.findViewById(R.id.aad)

        //  Se viene cliccato il bottono fa visualizare il miniplayer
        miniPlayerButton.setOnClickListener() {
            movePlayerUP()
            moveMiniPlayerButtonUP()
            moveAddTracksVideosButtonUP()
            moveMiniPlayerDownButtonUP()
            miniPlayerButton.visibility =View.GONE
            miniPlayerDownButton.visibility =View.VISIBLE
        }

        //  Se viene cliccato il bottono nasconde il miniplayer
        miniPlayerDownButton.setOnClickListener() {
            movePlayerDOWN()
            moveMiniPlayerButtonDOWN()
            moveAddTracksVideosButtonDOWN()
            moveMiniPlayerDownButtonDOWN()
            miniPlayerDownButton.visibility =View.GONE
            miniPlayerButton.visibility =View.VISIBLE
        }


        //Dichiarazzione delle variabili e valorizzazione
        var play = activityMain.findViewById(R.id.play_main) as ImageButton
        var pause = activityMain.findViewById(R.id.pause_main) as ImageButton
        var skipNext = activityMain.findViewById(R.id.skip_main) as ImageButton
        var nameOfTrack = activityMain.findViewById(R.id.trackNameId) as TextView

        //  Se è stata selezionato un audio pongo al TextView del miniplayer il nome dell'audio selezionato
        if (music.trackName != "") {
            nameOfTrack.text = music.trackName
        }   //  Se NON è stata selezionato un audio pongo al TextView del miniplayer "No track selected"
        else {
            nameOfTrack.text = "No track selected"
        }

        //  Se l'audio è in riproduzione setto la visibilità di 2 bottoni in un modo
        if (music.musicPlayer.isPlaying) {
            pause.visibility = View.VISIBLE
            play.visibility = View.GONE
        }  //  Se l'audio NON è in riproduzione setto la visibilità dei 2 bottoni in un modo diverso
        else {
            pause.visibility = View.GONE
            play.visibility = View.VISIBLE
        }

        //  Se viene cliccato il bottone play modifica la grafica e fa partire l'audio
        play.setOnClickListener() {
            if (music.idTrack != null) {
                play.visibility = View.GONE
                pause.visibility = View.VISIBLE
                video.videoPlayer!!.pause()
                music.startMusic()
            } else {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        //  Se viene cliccato il bottone play modifica la grafica e mette in pausa l'audio
        pause.setOnClickListener() {
            if (music.idTrack != null) {
                play.visibility = View.VISIBLE
                pause.visibility = View.GONE
                music.pauseMusic()
            } else {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            }
        }

        //  Se viene cliccato il bottone skipNext fa una serie di controlli prima di passare al prossimo audio
        skipNext.setOnClickListener() {
            fileList = dbHandler!!.readMP3Tracks()
            if (music.idTrack == null) {
                Toast.makeText(thiscontext, "Nessuna traccia selezionata", Toast.LENGTH_LONG).show()
            } else {
                if (fileList!!.size != 0) {         //Controlla se la lista di musica contiene almeno un elemento
                    play.visibility = View.GONE
                    pause.visibility = View.VISIBLE
                    var newPath = getNextTrack(music.idTrack!!)
                    if (newPath != null) {
                        music.skipForward(thiscontext, Uri.parse(newPath))
                        nameOfTrack.text = music.trackName
                    } else {
                        music.idTrack = fileList!![0].id
                        music.trackName = fileList!![0].fileName.toString()
                        newPath = fileList!![0].filePath
                        music.createMusic(thiscontext, Uri.parse(newPath))
                        music.startMusic()
                        nameOfTrack.text = music.trackName
                    }
                } else {
                    music.pauseMusic()
                    pause.visibility = View.GONE
                    play.visibility = View.VISIBLE
                }
            }
        }

        //Dichiarazzione delle variabili e valorizzazione
        var mp3 = activityMain.findViewById(R.id.MP3filter) as ImageButton
        var mp4 = activityMain.findViewById(R.id.MP4filter) as ImageButton
        var allFiles = activityMain.findViewById(R.id.all) as ImageButton
        var deleteFilter = activityMain.findViewById(R.id.deleteFilter) as ImageButton

        //  Se viene cliccato il bottone mp3 modifico la variabile di controllo per la visualizzazione dei file
        mp3.setOnClickListener {
            supportVars.typeDisplay = 1
            requireFragmentManager().beginTransaction()
                .replace(R.id.viewer, FragmentMain())
                .commit()
        }

        //  Se viene cliccato il bottone mp4 modifico la variabile di controllo per la visualizzazione dei file
        mp4.setOnClickListener {
            supportVars.typeDisplay = 2
            requireFragmentManager().beginTransaction()
                .replace(R.id.viewer, FragmentMain())
                .commit()
        }

        //  Se viene cliccato il bottone allFiles modifico la variabile di controllo per la visualizzazione dei file
        allFiles.setOnClickListener {
            supportVars.typeDisplay = 0
            requireFragmentManager().beginTransaction()
                .replace(R.id.viewer, FragmentMain())
                .commit()
        }

        //  Se viene cliccato il bottone deleteFilter modifico la variabile di controllo per la visualizzazione dei bottoni delete
        deleteFilter.setOnClickListener {
            supportVars.delBtnControl = !supportVars.delBtnControl
            requireFragmentManager().beginTransaction()
                .replace(R.id.viewer, FragmentMain())
                .commit()
        }

        return activityMain
    }


    companion object {
        fun newInstance() = FragmentMain()
    }


    //  Funzione che richiede e controlla i permessi e richiama la pagina di selezione dei file da aggiungere all'app
    fun requestRuntimePermission() {

        if (ActivityCompat.checkSelfPermission(
                thiscontext,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 113)
        } else {
            val intent: Intent = Intent(thiscontext, SassariMusic::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(thiscontext, "Permission Granted", Toast.LENGTH_LONG).show()
                val intent: Intent = Intent(thiscontext, SassariMusic::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(thiscontext, "Permission Denied", Toast.LENGTH_LONG).show()
            }
    }

    //  Prendi la prossima track della playlist (l'ultima track fa ricominciare la playlist)
    fun getNextTrack(id: Int): String? {
        var path: String? = null
        dbHandler = AudioAndVideoDatabaseHandler(thiscontext)

        for (i in 0 until fileList!!.size) {
            if (fileList!![i].id == id) {
                if (i == fileList!!.size - 1) {
                    music.idTrack = fileList!![0].id
                    music.trackName = fileList!![0].fileName.toString()
                    path = fileList!![0].filePath
                    return path
                } else {
                    music.idTrack = fileList!![i + 1].id
                    music.trackName = fileList!![i + 1].fileName.toString()
                    path = fileList!![i + 1].filePath
                    return path
                }
            }
        }
        return path
    }


    //  Funzione per gestire l'animazione del miniplayer
    fun movePlayerUP() {
        ObjectAnimator.ofFloat(miniPlayer, "translationY", -200f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun movePlayerDOWN() {
        ObjectAnimator.ofFloat(miniPlayer, "translationY", 0f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveMiniPlayerButtonUP() {
        ObjectAnimator.ofFloat(aac, "translationY", -200f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveMiniPlayerButtonDOWN() {
        ObjectAnimator.ofFloat(aac, "translationY", 0f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveMiniPlayerDownButtonUP() {
        ObjectAnimator.ofFloat(aad, "translationY", -200f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveMiniPlayerDownButtonDOWN() {
        ObjectAnimator.ofFloat(aad, "translationY", 0f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveAddTracksVideosButtonUP() {
        ObjectAnimator.ofFloat(aaa, "translationY", -200f).apply {
            duration = 700
            start()
        }
    }

    //  Funzione per gestire l'animazione del miniplayer
    fun moveAddTracksVideosButtonDOWN() {
        ObjectAnimator.ofFloat(aaa, "translationY", 0f).apply {
            duration = 700
            start()
        }
    }
}
