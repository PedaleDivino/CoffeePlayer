package com.example.mediaplayer.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class AudioAndVideoDatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_TODO_TABLE="CREATE TABLE $TABLE_FILE ($KEY_FILE_ID INTEGER PRIMARY KEY, $KEY_FILE_NAME TEXT, $KEY_FILE_TYPE TEXT, $KEY_FILE_PATH TEXT)"
        db?.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FILE")
        onCreate(db)
    }


    //  FUNZIONI CHE LAVORANO SULLA TABELLA DEI FILE

    /*
    CREATE
     */

    //  Funzione che mi aggiunge un nuovo elemento alla tabella dei file
    fun createFile(file: AudioAndVideo) {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_FILE_NAME, file.fileName)
        values.put(KEY_FILE_TYPE, file.fileType)
        values.put(KEY_FILE_PATH, file.filePath)

        db.insert(TABLE_FILE, null, values)

        //Log.d("DATA INSERITI", "SUCCESS")
        db.close()
    }



    /*
    READ
     */

    //  Funzione che ritorna un file che ha come ID l'intero passato come parametro
    @SuppressLint("Range")
    fun readAtodo(id: Int): AudioAndVideo{
        var db: SQLiteDatabase = readableDatabase
        var cursor: Cursor = db.query(TABLE_FILE, null, KEY_FILE_ID+"=?", arrayOf(id.toString()), null, null, null, null)

        cursor.moveToFirst()

        var file = AudioAndVideo()
        file.fileName = cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME))
        file.fileType = cursor.getString(cursor.getColumnIndex(KEY_FILE_TYPE))
        file.filePath = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH))

        return file
    }


    //  Funzione che ritorna un array di file contenente tutti i file che abbiamo aggiunto alla tabella
    @SuppressLint("Range")
    fun readToDo(): ArrayList<AudioAndVideo> {
        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<AudioAndVideo> = ArrayList()
        var selectAll = "SELECT * FROM $TABLE_FILE"
        var cursor: Cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                var file=AudioAndVideo()
                file.id=cursor.getInt(cursor.getColumnIndex(KEY_FILE_ID))
                file.fileName = cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME))
                file.fileType = cursor.getString(cursor.getColumnIndex(KEY_FILE_TYPE))
                file.filePath = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH))

                list.add(file)
            } while (cursor.moveToNext())
        }
        return list
    }


    //  Funzione che controlla se la tabella file ha almeno un elemento
    fun pieno(): Boolean {
        var db:SQLiteDatabase=readableDatabase
        var istruzione = "SELECT * FROM $TABLE_FILE"
        var cursorVuoto: Cursor = db.rawQuery(istruzione, null)
        return cursorVuoto.moveToFirst()
    }


    /*
    UPDATE
     */


    //  Funzione che rimpiazza un file del database con un altro che viene passato come parametro
    fun replaceFile(file: AudioAndVideo): Int {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_FILE_NAME, file.fileName)
        values.put(KEY_FILE_TYPE, file.fileType)
        values.put(KEY_FILE_PATH, file.filePath)

        /*
        UPDATE = AGGIORNA UNA RIGA DEL DATABASE (HA BISOGNO DI 4 ARGOMENTI)
        PRIMO ARGOMENTO = NOME DELLA TABELLA CHE VOGLIO MODIFICARE
        SECONDO ARGOMENTO = VALORI CHE VOGLIO AGGIORNARE
        TERZO ARGOMENTO = LA RIGA DA AGGIORNARE
        QUARTO ARGOMENTO = GLI ELEMENTI CHE VERRANNO SOSTITUITI DA I NUOVI VALORI
         */
        return db.update(TABLE_FILE, values, KEY_FILE_ID+"=?", arrayOf(file.id.toString()))
    }


    /*
    DELETE
     */


    //  Funzione che elimina dal database un file che ha come ID il valore passato come parametro
    fun deleteFile(id: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_FILE, KEY_FILE_ID+"=?", arrayOf(id.toString()))

        db.close()
    }



    //DUAGIDUGADHAWUID
    /*var id: Int?=null
    var playlistName: String?=null
    var playlist: ArrayList<AudioAndVideo> = ArrayList()*/

    fun translatePlaylistToString(playlist: Playlist): String {
        var stringedPlaylist: String
        stringedPlaylist = "${playlist.id}:${playlist.playlistName}:"

        for (i in 0 until playlist.playlistElements.size-1) {
            stringedPlaylist = "${stringedPlaylist}${playlist.playlistElements[i]}-"
        }

        var num = playlist.playlistElements.size-1

        stringedPlaylist = "${stringedPlaylist}${playlist.playlistElements[num]}"

        return stringedPlaylist
    }

    fun translateStringToPlaylist(stringedPlaylist: String): Playlist {
        var playlist: Playlist = Playlist()

        var lista: List<String> = stringedPlaylist.split(":")

        playlist.id = lista[0].toInt()
        playlist.playlistName = lista[1]

        var listaPlaylist: List<String> = lista[2].split("-")

        //playlist.playlistElements = ArrayList<String>(Arrays.asList(listaPlaylist))

        //ArrayList<String> listaString = new ArrayList<String>(Arrays.asList("1", "12"));
        val listaString: ArrayList<String> = ArrayList<String>(Arrays.asList("1", "12"))

        return playlist
    }

}