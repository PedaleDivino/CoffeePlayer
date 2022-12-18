package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler


class SassariMusic : AppCompatActivity() {
    val CHOOSE_FROM_DEVICE = 1001   //Variabile che definisce il tipo di richiesta
    var dbHandler : AudioAndVideoDatabaseHandler?= null //Variabile per la gestione del database
    var trackList: ArrayList<AudioAndVideo> = ArrayList()   //ArrayList che conterrà tutti gli elementi all'interno del database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callChooseFileFromDevice()  //Al momento dell'avvio dell'activity
    }

    //Codice preso e riadattato da stackoverflow, link: https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
    @SuppressLint("Range")
    fun getAudioPath(uri: Uri?): String? {
        if ("com.android.externalstorage.documents".equals(uri!!.getAuthority())){  //Codice preso da stackoverflow, Link: https://stackoverflow.com/questions/17546101/get-real-path-for-uri-android
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        }
        var cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Audio.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
        cursor.close()
        return path
    }

    //Codice preso e riadattato da stackoverflow, link: https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework
    @SuppressLint("Range")
    fun getVideoPath(uri: Uri?): String? {
        if ("com.android.externalstorage.documents".equals(uri!!.getAuthority())){  //Codice preso da stackoverflow, Link: https://stackoverflow.com/questions/17546101/get-real-path-for-uri-android
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":").toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        }
        var cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Video.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        cursor.close()
        return path
    }

    private fun callChooseFileFromDevice(){
        val intent : Intent = Intent(Intent.ACTION_OPEN_DOCUMENT)   //Crea un intent per l'apertura del file system del telefono
        val mimetypes = arrayOf("audio/mpeg", "video/mp4")  //Array che contiene i tipi di file che verranno visualizzati all'interno del file explorer e che saranno di conseguenza selezionabili(In questo caso MP3 e MP4)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes) //Inserisce la tipologia di file scelti all'interno dell'intent creato precedentemente
        startActivityForResult(intent, CHOOSE_FROM_DEVICE)  //Avvia l'activity per la ricezione di informazioni, in questo caso l'uri del file selezionato dall'utente
    }

    //Funzione per ottenere dati dall'activity del file explorer
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_FROM_DEVICE && resultCode == RESULT_OK) { //Controlla se il codice di richiesta è quello giusto e se l'activity è terminata con successo
            if (data != null) {
                val uri: Uri? = data.data //Prende la uri del file selezionato
                val cR: ContentResolver = this.getContentResolver()
                val type = cR.getType(uri!!)    //Ritorna il tipo del file selezionato
                var path : String? = ""
                if (type == "audio/mpeg"){
                    path = getAudioPath(uri)    //Esegue la conversione della uri un una path
                } else {
                    path = getVideoPath(uri)    //Esegue la conversione della uri un una path
                }
                dbHandler = AudioAndVideoDatabaseHandler(this)  //Viene inizializzato un oggetto di tipo AudioAndVideoDatabaseHandler per la gestione del database
                trackList = dbHandler!!.readTracks()    //Tramite il metodo readTracks vengono presi tutti i dati dal database e convertiti in un oggetto di tipo ArrayList
                var controllo = true
                for (i in 0 until trackList.size) {
                    if (trackList[i].filePath == path) {    //Controllo se la path non è già stata inserita all'interno del database
                        controllo = false
                        break
                    }
                }
                if (controllo) {    //Se non è stata inserita andrà ad avviare l'activity add_tracks per l'inserimento del nome del file e l'aggiunta al database
                    val addTracksInt : Intent = Intent(this, add_tracks::class.java)
                    addTracksInt.putExtra("Path", path) //Passa la path alla activity add_tracks
                    addTracksInt.putExtra("Type", type) //Passa il tipo del file alla activity add_tracks
                    startActivity(addTracksInt) //Avvia l'activity add_traks
                }
                else {  //Se la path è già stata selezionata avvertirà l'utente e riavvierà l'activity per permettere all'utente di selezionare un nuovo file
                    Toast.makeText(this, "FILE GIA SELEZIONATO", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SassariMusic::class.java))
                }
            }
        } else {    //Se il file explorer viene chiuso prima della selezione di un file verrà riavviata l'activity main
            Toast.makeText(this, "Non è stato selezionato nessun file", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}