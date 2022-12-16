package com.example.mediaplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler

class add_tracks : AppCompatActivity() {
    var dbHandler : AudioAndVideoDatabaseHandler ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracks)
        val extras = intent.extras
        dbHandler = AudioAndVideoDatabaseHandler(this)
        val fileNameEdit : EditText = findViewById(R.id.FileName)
        val addTracks : Button = findViewById(R.id.AddTrack)
        var extraUri : String= ""
        var typeFile : String = ""
        if (extras != null) {
            extraUri = extras.getString("Path").toString()
            typeFile = extras.getString("Type").toString()
        }
        addTracks.setOnClickListener(){
            if (fileNameEdit.text.isBlank()){
                fileNameEdit.setError("Non può essere lasciato vuoto")
            } else {
                val fileName : String = fileNameEdit.text.toString()
                if (typeFile == "audio/mpeg"){
                    val file = AudioAndVideo(null, fileName, "mp3", extraUri)
                    dbHandler!!.createFile(file)
                    Toast.makeText(this, "File audio aggiunto", Toast.LENGTH_LONG).show()
                } else {
                    val file = AudioAndVideo(null, fileName, "mp4", extraUri)
                    dbHandler!!.createFile(file)
                    Toast.makeText(this, "File video aggiunto", Toast.LENGTH_LONG).show()
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}