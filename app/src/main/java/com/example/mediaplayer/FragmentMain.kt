package com.example.mediaplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import kotlinx.android.synthetic.main.activity_main.view.*

class FragmentMain() : Fragment() {

    var disc: ImageView? = null
    var dbHandler : AudioAndVideoDatabaseHandler ?= null
    private var adapter: AudioAndVideoAdapter?=null
    private var fileList: ArrayList<AudioAndVideo>?=null
    private var fileListItem: ArrayList<AudioAndVideo>?=null
    private var layoutManager: RecyclerView.LayoutManager?=null
    lateinit var thiscontext : Context
    var music: AudioHandler = AudioHandler


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var activityMain : View = inflater.inflate(R.layout.activity_main, container, false)


        thiscontext = container!!.context
        fileListItem = ArrayList<AudioAndVideo>()
        adapter = AudioAndVideoAdapter(fileListItem!!, thiscontext, this) //INIZIALIZZO LA VARIAILE ADAPTER INSERENDO IL VALORE DELLA CLASSE ToDoListApadter (CHE HA BISOGNO DI ARGOMENTI)
        fileList = ArrayList<AudioAndVideo>()
        layoutManager = LinearLayoutManager(thiscontext) //INIZIALIZZO LAYOUTMANAGER CON UN MANAGER DI TIPO VERTICALE (HA BISOGNO DEL CONTESTO)

        activityMain.recyclerViewId.layoutManager=layoutManager //ASSOCIO IL LAYOUT MANAGER DA ME CREATO CON IL RECYCLEVIEWID DELL'ACTIVITY
        activityMain.recyclerViewId.adapter=adapter //ASSOCIO L'ADAPTER DA ME CREATO CON QUELLO DEL RECYCLEVIEWID DELL'ACTIVITY

        dbHandler= AudioAndVideoDatabaseHandler(thiscontext)

        fileList = dbHandler!!.readTracks()
        fileList!!.reverse()

        for(t in fileList!!.iterator()) {
            val file = AudioAndVideo()
            file.fileName = t.fileName
            file.id = t.id
            file.filePath = t.filePath
            file.fileType = t.fileType

            //Log.d("Lista", t.toDoName.toString())

            fileListItem!!.add(file)
        }

        fileList!!.reverse()

        //disc = findViewById<ImageView>(R.id.albumImageId)
        //discAnimation()
        val bottone : Button = activityMain.findViewById(R.id.aaa)
        bottone.setOnClickListener(){
            requestRuntimePermission()
        }

        var play = activityMain.findViewById(R.id.play_main) as ImageButton
        var pause = activityMain.findViewById(R.id.pause_main) as ImageButton
        var skipNext = activityMain.findViewById(R.id.skip_main) as ImageButton
        var nameOfTrack = activityMain.findViewById(R.id.trackNameId) as TextView

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
                    music.idTrack = fileList!![0].id
                    music.trackName = fileList!![0].fileName.toString()
                    newPath = fileList!![0].filePath
                    music.createMusic(thiscontext, Uri.parse(newPath))
                    music.startMusic()
                    nameOfTrack.text = music.trackName
                }
                Log.d("ID TRACK dopo" , music.idTrack.toString())
            }
        }

        return activityMain
    }


    companion object {
        //var music: MediaPlayer = MediaPlayer()
        fun newInstance() = FragmentMain()
    }


    fun requestRuntimePermission(){

        if (ActivityCompat.checkSelfPermission(thiscontext, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), 113)
        }
        else {
            val intent: Intent = Intent(thiscontext, SassariMusic::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(thiscontext, "Permission Granted", Toast.LENGTH_LONG).show()
                val intent: Intent = Intent(thiscontext, SassariMusic::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(thiscontext, "Permission Denied", Toast.LENGTH_LONG).show()
            }
    }

    //  Prendi la prossima track della playlist (l'ultima track fa ricominciare la playlist)
    fun getNextTrack(id: Int): String? {
        var path: String ?= null
        for (i in 0 until fileList!!.size) {
            if (fileList!![i].id == id) {
                if (i == fileList!!.size-1) {
                    music.idTrack = fileList!![0].id
                    music.trackName = fileList!![0].fileName.toString()
                    path = fileList!![0].filePath
                    return path
                }
                else {
                    music.idTrack = fileList!![i+1].id
                    music.trackName = fileList!![i+1].fileName.toString()
                    path = fileList!![i+1].filePath
                    return path
                }
            }
        }
        return path
    }
}

/*

class MainActivity : AppCompatActivity() {




    fun requestRuntimePermission(){
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 113)
        }
        else {
            val intent: Intent = Intent(this, SassariMusic::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    val intent: Intent = Intent(this, SassariMusic::class.java)
                    startActivity(intent)
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
    }

}

 */
