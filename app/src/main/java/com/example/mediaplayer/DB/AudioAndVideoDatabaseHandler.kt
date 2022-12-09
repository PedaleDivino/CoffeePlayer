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



    //  FUNZIONI CHE LAVORANO SULLA TABELLA DELLE PLAYLIST

    /*
    CREATE
     */

    //  Funzione che aggiunge una playlist alla tabella delle playlist (la playlist è passata come parametro)
    fun createPlaylist(playlist: Playlist) {
        var db: SQLiteDatabase = writableDatabase

        var stringedPlaylist = translatePlaylistElementsToString(playlist.playlistElements)

        var values: ContentValues = ContentValues()
        values.put(KEY_PLAYLIST_NAME, playlist.playlistName)
        values.put(KEY_PLAYLIST_FILE, stringedPlaylist)

        db.insert(TABLE_FILE, null, values)

        db.close()
    }


    /*
    READ
     */

    //  Funzione che ritorna una playlist
    //  (l'ID della playlist è passata come parametro)
    @SuppressLint("Range")
    fun readAPlaylist(id: Int): Playlist {
        var db: SQLiteDatabase = readableDatabase
        var cursor: Cursor = db.query(TABLE_PLAYLIST, null, KEY_FILE_ID+"=?", arrayOf(id.toString()), null, null, null, null)

        cursor.moveToFirst()

        var playlist = Playlist()

        playlist.id = id
        playlist.playlistName = cursor.getString(cursor.getColumnIndex(KEY_PLAYLIST_NAME))

        var stringedPlaylistElements = cursor.getString(cursor.getColumnIndex(KEY_PLAYLIST_FILE))
        playlist.playlistElements = translateStringToPlaylistElements(stringedPlaylistElements)

        return playlist
    }

    //  Funzione che ritorna un array di playlist contenente tutti le playlist che abbiamo aggiunto alla tabella
    @SuppressLint("Range")
    fun readPlaylists(): ArrayList<Playlist> {
        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<Playlist> = ArrayList()
        var selectAll = "SELECT * FROM $TABLE_PLAYLIST"
        var cursor: Cursor = db.rawQuery(selectAll, null)

        if (cursor.moveToFirst()) {
            do {
                var playlist=Playlist()
                playlist.id=cursor.getInt(cursor.getColumnIndex(KEY_PLAYLIST_ID))
                playlist.playlistName = cursor.getString(cursor.getColumnIndex(KEY_PLAYLIST_NAME))

                var stringedPlaylistElements = cursor.getString(cursor.getColumnIndex(KEY_PLAYLIST_FILE))
                playlist.playlistElements = translateStringToPlaylistElements(stringedPlaylistElements)

                list.add(playlist)
            } while (cursor.moveToNext())
        }
        return list
    }


    /*
    UPDATE
     */

    //  Funzione che aggiunge un elemento a una playlist
    //  (la playlist e l'id del file dell'elemento sono passati per parametro)
    fun addElementToPlaylist(playlist: Playlist, idFile: Int) {
        var db: SQLiteDatabase = writableDatabase

        playlist.playlistElements.add(idFile.toString())

        var stringedPlaylistElements = translatePlaylistElementsToString(playlist.playlistElements)

        var update = ContentValues()
        update.put(KEY_PLAYLIST_FILE, stringedPlaylistElements)
        db.update(TABLE_PLAYLIST, update, KEY_PLAYLIST_ID+"=?", arrayOf(playlist.id.toString()))

        db.close()
    }

    /*
    DELETE
     */

    //  Funzione che elimina dal database una playlist che ha come ID il valore passato come parametro
    fun deletePlaylist(id: Int) {
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_PLAYLIST, KEY_FILE_ID+"=?", arrayOf(id.toString()))

        db.close()
    }

    //  Funzione che elimina un elemento dalla playlist
    //  (la playlist e l'id del file dell'elemento sono passati per parametro)
    fun removeElementFromPlaylist(playlist: Playlist, idFile: Int) {
        var db: SQLiteDatabase = writableDatabase

        var idFileStringed = idFile.toString()

        for (i in 0 until playlist.playlistElements.size) {
            if (playlist.playlistElements[i]==idFileStringed) {
                playlist.playlistElements.removeAt(i)
                break
            }
        }
        var stringedPlaylistElements = translatePlaylistElementsToString(playlist.playlistElements)

        var update = ContentValues()
        update.put(KEY_PLAYLIST_FILE, stringedPlaylistElements)
        db.update(TABLE_PLAYLIST, update, KEY_PLAYLIST_ID+"=?", arrayOf(playlist.id.toString()))

        db.close()
    }


    /*
    FUNZIONI DI APPOGGIO
     */

    //  Funzione che ritorna gli elementi di una playlist trasformati in una stringa per semplificare la loro gestione nel database
    //  (vengono passati come parametro gli elementi della playlist)
    fun translatePlaylistElementsToString(playlistElements: ArrayList<String>): String {
        var stringedPlaylistElements: String

        stringedPlaylistElements = "${playlistElements[0]}-"

        for (i in 1 until playlistElements.size-1) {
            stringedPlaylistElements = "${stringedPlaylistElements}${playlistElements[i]}-"
        }

        var num = playlistElements.size-1

        stringedPlaylistElements = "${stringedPlaylistElements}${playlistElements[num]}"

        return stringedPlaylistElements
    }

    //  Funzione che ritorna la stringa contenente gli elementi della playlist trasformata in una arraylist di stringhe
    //  (la stringa contenente gli elementi della playlist è passata come parametro)
    fun translateStringToPlaylistElements(stringedPlaylist: String): ArrayList<String> {

        var listaPlaylist: List<String> = stringedPlaylist.split("-")

        var stringedPlaylistElements: ArrayList<String> = ArrayList<String>(listaPlaylist)

        return stringedPlaylistElements
    }

}