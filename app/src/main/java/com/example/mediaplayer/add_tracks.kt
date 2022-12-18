package com.example.mediaplayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import java.io.File

class add_tracks : AppCompatActivity() {
    var dbHandler : AudioAndVideoDatabaseHandler ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tracks)
        val extras =
            intent.extras  //Inizializza una variabile per recuperare i valori passati dall'activity precedente
        dbHandler = AudioAndVideoDatabaseHandler(this)
        val fileNameEdit: EditText =
            findViewById(R.id.FileName)   //Associa l'EditText FileName ad una variabile
        val addTracks: Button =
            findViewById(R.id.AddTrack)    //Associa il bottone AddTrack ad una variabile
        var extraUri: String = ""
        var typeFile: String = ""
        if (extras != null) {   //Controlla se ci sono dei valori passati dall'activity precedente
            //Tramite una key identificativa assegnata ad ogni valore per essere trasmesso i valori vengono assegnati a delle variabili locali
            extraUri = extras.getString("Path").toString()
            typeFile = extras.getString("Type").toString()
        }
        addTracks.setOnClickListener() {
            var fileName: String = ""
            if (fileNameEdit.text.isBlank()) {   //Controllo se l'EditText è stato lasciato vuoto
                fileName = File(extraUri).name  //Se l'EditText è stato lasciato vuoto prende il nome del file originale
            } else {
                fileName = fileNameEdit.text.toString() //Prende la stringa inserita all'interno dell'editText dall'utente
            }
            if (typeFile == "audio/mpeg") {
                if (!fileName.contains(".mp3")) {    //Controlla se fileName non contiene l'estensione del file .mp3
                    fileName += ".mp3"  //Aggiunge al nome del file .mp3
                }
                val file = AudioAndVideo(null, fileName, "mp3", extraUri)
                dbHandler!!.createFile(file)    //Richiama il metodo createFile per l'aggiunta del nuovo file al database
                Toast.makeText(this, "File audio aggiunto", Toast.LENGTH_LONG).show()
            } else {
                if (!fileName.contains(".mp4")){ //Controlla se fileName non contiene l'estensione del file .mp4
                        fileName += ".mp4"  //Aggiunge al nome del file .mp3
                }
                val file = AudioAndVideo(null, fileName, "mp4", extraUri)
                dbHandler!!.createFile(file)    //Richiama il metodo createFile per l'aggiunta del nuovo file al database
                Toast.makeText(this, "File video aggiunto", Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(this, MainActivity::class.java))   //Avvia la MainActivity
        }
    }
}