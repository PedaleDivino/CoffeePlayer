package com.example.mediaplayer

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler

class AudioAndVideoAdapter(private var list: ArrayList<AudioAndVideo>, private val context: Context): RecyclerView.Adapter<AudioAndVideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_tracks, parent, false)
        return ViewHolder(view)
    }
    /* ONCREATEVIEWHOLDER: FUNZIONE CHE MI PREMETTE DI IDENTIFICARE IL LAYOUT DA COPIARE (QUINDI IL LIST_ROW) E SAPERE DOVE INSERIRLO
    * INFATTI MI RESTITUISCE UN VALORE CHE DOVRò UTILIZZARE IN SEGUITO, O DA UN'ALTRA PARTE*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }
    /* ONBINDVIEWHOLDER: FUNZIONE CHE MI PREMETTE DI SAPERE QUALE ELEMENTO/I PRENDERE (IN QUESTO CASO IL TODONAME) E IN QUALE POSIZIONE INSERIRLO/I*/

    override fun getItemCount(): Int {
        return list.size
    }
    /* GETITEM COUNT: FUNZIONE CHE MI PERMETTE DI CONTARE GLI ELEMENTI ALL'INTERNO DELLA LISTA*/


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var fileList = list
        var fileName = itemView.findViewById(R.id.trackNameId) as TextView
        var filePath = itemView.findViewById(R.id.trackPathId) as TextView

        var dbHandler : AudioAndVideoDatabaseHandler?= null

        fun bindView(file: AudioAndVideo) {
            fileName.text = file.fileName
            filePath.text = file.filePath
            fileName.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            var todoPosition: Int = adapterPosition
            when(p0!!.id) {
                fileName.id -> {
                    var file = getFile(fileList[todoPosition].id!!)
                    val uri: Uri =  Uri.parse(file.filePath.toString())

                    Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show()
                    val musicPlayer = MediaPlayer.create(context, uri)

                    if (musicPlayer!=null) {
                        musicPlayer.start()
                    }
                    else {
                        Toast.makeText(context, "no work", Toast.LENGTH_LONG).show()
                    }
                }


            }
        }

        fun getFile(id: Int): AudioAndVideo {
            var db: AudioAndVideoDatabaseHandler = AudioAndVideoDatabaseHandler(context)
            return db.readAtodo(id)
        }
    }
}