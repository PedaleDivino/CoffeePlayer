package com.example.mediaplayer

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var activityMain : View = inflater.inflate(R.layout.activity_main, container, false)

        var music = (activity as MainActivity).musicMain


        thiscontext = container!!.context
        fileListItem = ArrayList<AudioAndVideo>()
        adapter = AudioAndVideoAdapter(fileListItem!!, thiscontext, music, this) //INIZIALIZZO LA VARIAILE ADAPTER INSERENDO IL VALORE DELLA CLASSE ToDoListApadter (CHE HA BISOGNO DI ARGOMENTI)
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

        //disc = findViewById<ImageView>(R.id.albumImageId)
        //discAnimation()
        val bottone : Button = activityMain.findViewById(R.id.aaa)
        bottone.setOnClickListener(){
            requestRuntimePermission()
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
