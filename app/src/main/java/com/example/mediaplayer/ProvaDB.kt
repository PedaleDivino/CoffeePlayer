package com.example.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import kotlinx.android.synthetic.main.activity_prova_db.*

class ProvaDB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prova_db)
        var dbHandler : AudioAndVideoDatabaseHandler = AudioAndVideoDatabaseHandler(this)
        button.setOnClickListener(){
            var file = dbHandler.readAtodo(2)
            textView.setText(file.fileName)
            textView2.setText(file.fileType)
            textView3.setText(file.filePath)
        }
    }
}