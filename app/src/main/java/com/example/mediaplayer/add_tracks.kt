package com.example.mediaplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class add_tracks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracks)
        val extras = intent.extras
        var extraUri = ""
        if (extras != null) {
            extraUri = extras.getString("Uri").toString()
        }
        val uri : Uri = Uri.parse(extraUri)
    }
}