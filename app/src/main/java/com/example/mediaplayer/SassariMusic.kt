package com.example.mediaplayer

import android.content.ContentResolver
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class SassariMusic : AppCompatActivity() {
    val CHOOSE_FROM_DEVICE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val add : Button = findViewById(R.id.AddButton)

        add.setOnClickListener(){
            callChooseFileFromDevice()
        }
    }

    private fun callChooseFileFromDevice(){
        val intent : Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        val mimetypes = arrayOf("audio/mpeg", "video/mp4")
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, CHOOSE_FROM_DEVICE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_FROM_DEVICE && resultCode == RESULT_OK) {
            if (data != null) {
                val uri: Uri? = data.data
                val cR: ContentResolver = this.getContentResolver()
                val type = cR.getType(uri!!)
                if (type == "audio/mpeg"){
                    val music = MediaPlayer.create(this, uri)
                    music.start()
                } else if (type == "video/mp4"){
                    val video : VideoView = findViewById(R.id.videoView2)
                    video.setVideoURI(uri)
                    video.start()
                    video.setMediaController(MediaController(this))
                }
                Toast.makeText(this, "MIME type: ", Toast.LENGTH_LONG).show()
            }
        }
    }
}