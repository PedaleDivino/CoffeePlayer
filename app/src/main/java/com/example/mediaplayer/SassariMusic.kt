package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler


class SassariMusic : AppCompatActivity() {
    val CHOOSE_FROM_DEVICE = 1001
    var dbHandler : AudioAndVideoDatabaseHandler?= null
    var trackList: ArrayList<AudioAndVideo> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callChooseFileFromDevice()
    }

    @SuppressLint("Range")
    fun getAudioPath(uri: Uri?): String? {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
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

    @SuppressLint("Range")
    fun getVideoPath(uri: Uri?): String? {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
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
                var path : String? = ""
                if (type == "audio/mpeg"){
                    path = getAudioPath(uri)
                    Toast.makeText(this, "File audio selezionato", Toast.LENGTH_LONG).show()
                } else {
                    path = getVideoPath(uri)
                    Toast.makeText(this, "File video selezionato", Toast.LENGTH_LONG).show()
                }
                dbHandler = AudioAndVideoDatabaseHandler(this)
                trackList = dbHandler!!.readTracks()
                var controllo = true
                for (i in 0 until trackList.size) {
                    if (trackList[i].filePath == path) {
                        controllo = false
                        break
                    }
                }
                if (controllo) {
                    val addTracksInt : Intent = Intent(this, add_tracks::class.java)
                    addTracksInt.putExtra("Path", path)
                    addTracksInt.putExtra("Type", type)
                    startActivity(addTracksInt)
                }
                else {
                    Toast.makeText(this, "FILE GIA SELEZIONATO", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, SassariMusic::class.java))
                }
            }
        } else {
            Toast.makeText(this, "Non è stato selezionato nessun file", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}