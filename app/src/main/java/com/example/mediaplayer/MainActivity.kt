package com.example.mediaplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var disc: ImageView? = null
    var dbHandler : AudioAndVideoDatabaseHandler ?= null
    private var adapter: AudioAndVideoAdapter?=null
    private var fileList: ArrayList<AudioAndVideo>?=null
    private var fileListItem: ArrayList<AudioAndVideo>?=null
    private var layoutManager: RecyclerView.LayoutManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHandler = AudioAndVideoDatabaseHandler(this)

        fileListItem = ArrayList<AudioAndVideo>()
        adapter = AudioAndVideoAdapter(fileListItem!!, this) //INIZIALIZZO LA VARIAILE ADAPTER INSERENDO IL VALORE DELLA CLASSE ToDoListApadter (CHE HA BISOGNO DI ARGOMENTI)
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
            file.fileType = t.filePath

            //Log.d("Lista", t.toDoName.toString())

            fileListItem!!.add(file)
        }



        //disc = findViewById<ImageView>(R.id.albumImageId)
        //discAnimation()
        val bottone : Button = findViewById(R.id.aaa)
        bottone.setOnClickListener(){
            requestRuntimePermission()
        }
        prova.setOnClickListener(){
            val intent: Intent = Intent(this, SassariMusic::class.java)
            startActivity(intent)
        }
        prova2.setOnClickListener(){
            val intent: Intent = Intent(this, ProvaDB::class.java)
            startActivity(intent)
        }
    }



    fun requestRuntimePermission() : Boolean {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 113)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
    }















    fun discAnimation() {

        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        rotate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val continueRotate = AnimationUtils.loadAnimation(this@MainActivity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }

}