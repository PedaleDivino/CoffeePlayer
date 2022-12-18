package com.example.mediaplayer

import android.content.ClipData.Item
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class AudioAndVideoAdapter(private var list: ArrayList<AudioAndVideo>, private val context: Context, var fragment: Fragment): RecyclerView.Adapter<AudioAndVideoAdapter.ViewHolder>() {

    var music: AudioHandler = AudioHandler
    var video: VideoHandler = VideoHandler
    var provona: Prova = Prova

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
        var fileDelete = itemView.findViewById(R.id.buttonDeleteId) as ImageButton


        var dbHandler : AudioAndVideoDatabaseHandler = AudioAndVideoDatabaseHandler(context)

        fun bindView(file: AudioAndVideo) {
            fileName.text = file.fileName
            fileName.setOnClickListener(this)
            fileDelete.setOnClickListener(this)
            if (provona.controllo){
                fileDelete.visibility = View.VISIBLE
            }
            else {
                fileDelete.visibility = View.INVISIBLE
            }
        }

        override fun onClick(p0: View?) {
            var filePosition: Int = absoluteAdapterPosition
            when(p0!!.id) {
                fileName.id -> {
                    var file = getFile(fileList[filePosition].id!!)
                    val uri: Uri =  Uri.parse(file.filePath.toString())

                    if (file.fileType == "mp3") {
                        if (video.videoPlayer!!.isPlaying) {
                            video.videoPlayer!!.pause()
                        }
                        music.idTrack = file.id
                        music.trackName = file.fileName.toString()
                        music.createMusic(context, uri)
                        music.startMusic()
                        Log.d("ID TRACK" , music.idTrack.toString())

                        changeFragmentOnMusicStart(fragment)
                    }

                    if (file.fileType == "mp4") {
                        if (music.musicPlayer.isPlaying) {
                            music.musicPlayer.pause()
                        }
                        video.startPlaylistInPosition(dbHandler.readMP4Tracks(), file.id!!)
                        provona.provino!!.visibility = BottomNavigationView.GONE

                        changeFragmentOnVideoStart(fragment)
                    }

                }

                fileDelete.id -> {
                    var file = getFile(fileList[filePosition].id!!)
                    if (file.fileType == "mp4") {
                        if(video.videoPlayer!!.mediaItemCount != 0)  {
                            video.removeFileFromExoPlayer(dbHandler.readMP4Tracks(), file.id!!)
                        }
                    }
                    dbHandler.deleteFile(file.id!!)
                    list.removeAt(filePosition)
                    notifyItemRemoved(filePosition)
                    Toast.makeText(context, "${file.fileName} è stato eliminato", Toast.LENGTH_SHORT).show()
                }

            }
        }

        fun getFile(id: Int): AudioAndVideo {
            dbHandler = AudioAndVideoDatabaseHandler(context)
            return dbHandler.readATrack(id)
        }

        fun changeFragmentOnMusicStart (fragment : Fragment) {
            fragment.requireActivity().supportFragmentManager.beginTransaction().replace(R.id.viewer, FragmentPlayer()).commit()
        }

        fun changeFragmentOnVideoStart (fragment : Fragment) {
            fragment.requireActivity().supportFragmentManager.beginTransaction().replace(R.id.viewer, FragmentVideo()).commit()
        }
    }
}