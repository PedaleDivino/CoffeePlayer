package com.example.mediaplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.viewer.*
import kotlinx.android.synthetic.main.viewer.bottom_navigation

//

class MainActivity : AppCompatActivity(){

    //var musicMain = MediaPlayer()

    var dbHandler : AudioAndVideoDatabaseHandler?= null
    private var adapter: AudioAndVideoAdapter?=null
    private var fileList: ArrayList<AudioAndVideo>?=null
    private var fileListItem: ArrayList<AudioAndVideo>?=null
    private var layoutManager: RecyclerView.LayoutManager?=null
    var musicPlayer: MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            //startActivity(Intent(this, FragmentMain::class.java))

            when (item.itemId){

                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.play -> {
                    startActivity(Intent(this, PlayerActivity::class.java))
                    true
                }
                else -> {
                    false
                }

            }
        }

        bottom_navigation.setOnNavigationItemReselectedListener { item ->
            //startActivity(Intent(this, FragmentMain::class.java))

            when (item.itemId){

                R.id.home -> {
                    //startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.play -> {
                    startActivity(Intent(this, PlayerActivity::class.java))
                    true
                }
                else -> {
                    false
                }

            }
        }



        fileListItem = ArrayList<AudioAndVideo>()
        adapter = AudioAndVideoAdapter(fileListItem!!, this, musicPlayer) //INIZIALIZZO LA VARIAILE ADAPTER INSERENDO IL VALORE DELLA CLASSE ToDoListApadter (CHE HA BISOGNO DI ARGOMENTI)
        fileList = ArrayList<AudioAndVideo>()
        layoutManager = LinearLayoutManager(this) //INIZIALIZZO LAYOUTMANAGER CON UN MANAGER DI TIPO VERTICALE (HA BISOGNO DEL CONTESTO)

        recyclerViewId.layoutManager=layoutManager //ASSOCIO IL LAYOUT MANAGER DA ME CREATO CON IL RECYCLEVIEWID DELL'ACTIVITY
        recyclerViewId.adapter=adapter //ASSOCIO L'ADAPTER DA ME CREATO CON QUELLO DEL RECYCLEVIEWID DELL'ACTIVITY

        dbHandler= AudioAndVideoDatabaseHandler(this)

        fileList = dbHandler!!.readToDo()
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

        val bottone : Button = findViewById(R.id.aaa)
        bottone.setOnClickListener(){
            requestRuntimePermission()
        }
    } // fine OnCreate



    fun requestRuntimePermission() {
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