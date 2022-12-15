package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DB.AudioAndVideo
import com.example.mediaplayer.DB.AudioAndVideoDatabaseHandler


class AudioAndVideoAdapter(private var list: ArrayList<AudioAndVideo>, private val context: Context, var musicPlayer: MediaPlayer): RecyclerView.Adapter<AudioAndVideoAdapter.ViewHolder>() {

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

                    var intent: Intent = Intent(context, PlayerActivity::class.java)

                    intent.putExtra("Name", fileName.text.toString())
                    intent.putExtra("Path", filePath.text.toString())

                    startActivity(context, intent, null)








                    /*var file = getFile(fileList[todoPosition].id!!)
                    val uri: Uri =  Uri.parse(file.filePath.toString())

                    Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show()

                    if (musicPlayer ==null) {
                        musicPlayer = MediaPlayer.create(context, uri)
                        musicPlayer.start()
                    }
                    else {
                        if (musicPlayer.isPlaying){
                            musicPlayer.stop()
                        }
                        musicPlayer = MediaPlayer.create(context, uri)
                        musicPlayer.start()

                        /*MainActivity().loadFragment(FragmentPlayer())

                        val fragment: Fragment = FragmentPlayer()
                        //var playerPlay : View = (R.layout.player_play)

                        val fragmentManager: FragmentManager = getActivity().getSupportFragmentManager()
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.layout.player_play, fragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()*/
                    }*/
                }


            }
        }

        fun getFile(id: Int): AudioAndVideo {
            var db: AudioAndVideoDatabaseHandler = AudioAndVideoDatabaseHandler(context)
            return db.readAtodo(id)
        }
    }
}