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
    //  (l'elemento da aggiungere alla tabella è passata come)
    fun createFile(file: AudioAndVideo) {
        var db: SQLiteDatabase = writableDatabase           //  Variabile che può accedere al database

        var values: ContentValues = ContentValues()         //  Variabile che conterrà tutti i dati che saranno inseriti nella tabella
        values.put(KEY_FILE_NAME, file.fileName)            //  Dico alla variabile "values" che deve salvare nella colonna "KEY_FILE_NAME" il nome del file passato come parametro
        values.put(KEY_FILE_TYPE, file.fileType)            //  Dico alla variabile "values" che deve salvare nella colonna "KEY_FILE_TYPE" il tipo del file passato come parametro
        values.put(KEY_FILE_PATH, file.filePath)            //  Dico alla variabile "values" che deve salvare nella colonna "KEY_FILE_PATH" la posizione in memoria del file passato come parametro

        db.insert(TABLE_FILE, null, values)         //  Inserisco nel database i valori che sono stati assegnati a "values"

        db.close()          //  Chiudo la connesione al database
    }


    /*
    READ
     */


    //  Funzione che ritorna un file che ha come ID l'intero passato come parametro
    @SuppressLint("Range")
    fun readAtodo(id: Int): AudioAndVideo{
        var db: SQLiteDatabase = readableDatabase           //  Variabile "db" di tipo SQLiteDatabase che può accedere al database

        var cursor: Cursor = db.query(          //  Variabile "cursor" di tipo Cursor che fornisce un interfaccia al risultato della query che poniamo al database e richiamo di una funzione query sul database
            TABLE_FILE,                         //  Indico a quale tabella stiamo facendo la query
            null,                       //  Indico che colonne voglio nel ritorno dell'operazione (ponendo null indico che voglio tutte le colonne)
            KEY_FILE_ID+"=?",           //  Indico che la condizione sull'ID del file sarà passato nel prossimo parametro
            arrayOf(id.toString()),             //  Indico l'ID del file che voglio trovare (il valore passato per parametro)
            null,                       //  Indico di non fare il groupBy
            null,                        //  Indico di non fare il having
            null,                       //  Indico di non fare l'orderBy
            null)                         //  Indico di non mettere un limite al numero di righe da restituire e chiudo il richiamo della funzione query

        cursor.moveToFirst()                   //  Punto il cursore alla prima riga del risultato che gli ha dato la query (in questo caso è l'unica riga)

        var file = AudioAndVideo()             //  Variabile "file" di tipo AudioAndVideo
        //NON SO ANCORA SE LA VOGLIAMO  file.id = cursor.getInt(cursor.getColumnIndex(KEY_FILE_ID))             //  Passo alla variabile file l'id del file desiderato
        file.fileName = cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME))          //  Passo alla variabile file il nome del file desiderato (preso dalla tabella dei file)
        file.fileType = cursor.getString(cursor.getColumnIndex(KEY_FILE_TYPE))          //  Passo alla variabile file il tipo del file desiderato (preso dalla tabella dei file)
        file.filePath = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH))          //  Passo alla variabile file il path del file desiderato (preso dalla tabella dei file)

        return file         //  Ritorno la variabile file riempita dai dati che abbiamo preso dal database tramite la query
    }


    //  Funzione che ritorna un array di file contenente tutti i file che abbiamo aggiunto alla tabella
    @SuppressLint("Range")
    fun readToDo(): ArrayList<AudioAndVideo> {
        var db: SQLiteDatabase = readableDatabase                              //  Variabile "db" di tipo SQLiteDatabase che può accedere al database
        var list: ArrayList<AudioAndVideo> = ArrayList()                       //  Variabile "list" che è un ArrayList di tipo AudioAndVideo che conterrà tutti i dati dei file salvati nel database
        var selectAll = "SELECT * FROM $TABLE_FILE"                            //  Variabile "selectAll" di tipo stringa che salva la query da porre al database (che in questo caso richiede tutta la tabella TABLE_FILE)
        var cursor: Cursor = db.rawQuery(selectAll, null)           //  Variabile "cursor" di tipo Cursor che fornisce un interfaccia al risultato della query che poniamo al database e richiamo di una funzione rawQuery sul database
        var file=AudioAndVideo()                                               //  Variabile "file" di tipo AudioAndVideo che serve per compilare i file prima di metterli nella lista

        if (cursor.moveToFirst()) {                                            //  Se il risultato della query non è vuota punta il cursore alla prima riga del risultato
            do {                                                                    //  Inizio di un ciclo do while
                file.id=cursor.getInt(cursor.getColumnIndex(KEY_FILE_ID))                       //  Passo alla variabile file l'id del file corrente
                file.fileName = cursor.getString(cursor.getColumnIndex(KEY_FILE_NAME))          //  Passo alla variabile file il nome del file corrente
                file.fileType = cursor.getString(cursor.getColumnIndex(KEY_FILE_TYPE))          //  Passo alla variabile file il tipe del file corrente
                file.filePath = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH))          //  Passo alla variabile file il path del file corrente

                list.add(file)                                                                  //  Aggiungo il file appena compilato alla lista
            } while (cursor.moveToNext())                                           //  Ripeto il ciclo se il cursore non ha finito di leggere i risultato della query e passa alla prossima riga
        }
        return list                                                            //  Ritorno l'ArrayList "list" che contiene tutti i file
    }


    //  Funzione che controlla se la tabella file ha almeno un elemento
    fun pieno(): Boolean {
        var db:SQLiteDatabase = readableDatabase                                   //  Variabile "db" di tipo SQLiteDatabase che può accedere al database
        var istruzione = "SELECT * FROM $TABLE_FILE"                               //  Variabile "istruzione" di tipo stringa che salva la query da porre al database (che in questo caso richiede tutta la tabella TABLE_FILE)
        var cursorVuoto: Cursor = db.rawQuery(istruzione, null)         //  Variabile "cursor" di tipo Cursor che fornisce un interfaccia al risultato della query che poniamo al database e richiamo di una funzione rawQuery sul database
        return cursorVuoto.moveToFirst()                                           //  Ritorno false se la query mi da un risultato vuoto, mentre ritorno vero se la query mi da un risultato non vuoto
    }


    /*
    UPDATE
     */


    //  Funzione che rimpiazza un file del database con un altro che viene passato come parametro
    fun replaceFile(file: AudioAndVideo) {
        var db: SQLiteDatabase = writableDatabase           //  Variabile "db" di tipo SQLiteDatabase che può accedere al database
        var values: ContentValues = ContentValues()         //  Variabile "values" di tipo ContentValues che conterrà i valori che sovrascriveranno i valori attualmente nel database
        values.put(KEY_FILE_NAME, file.fileName)            //  Pongo a values il nuovo valore della colonna KEY_FILE_NAME (cioè il nome del file che è stato passato come parametro)
        values.put(KEY_FILE_TYPE, file.fileType)            //  Pongo a values il nuovo valore della colonna KEY_FILE_TYPE (cioè il tipo del file che è stato passato come parametro)
        values.put(KEY_FILE_PATH, file.filePath)            //  Pongo a values il nuovo valore della colonna KEY_FILE_PATH (cioè il path del file che è stato passato come parametro)

        /*
        UPDATE = AGGIORNA UNA RIGA DEL DATABASE (HA BISOGNO DI 4 ARGOMENTI)
        PRIMO ARGOMENTO = NOME DELLA TABELLA CHE VOGLIO MODIFICARE
        SECONDO ARGOMENTO = VALORI CHE VOGLIO AGGIORNARE
        TERZO ARGOMENTO = LA RIGA DA AGGIORNARE
        QUARTO ARGOMENTO = GLI ELEMENTI CHE VERRANNO SOSTITUITI DA I NUOVI VALORI
         */
        db.update(                                          //  Richiamo una funzione per aggiornare il database
            TABLE_FILE,                                         //  Indico a quale tabella stiamo facendo la query
            values,                                             //  Passo i nuovi valori che devono sovrascrivere i valori vecchi
            KEY_FILE_ID+"=?",                        //  Passo
            arrayOf(file.id.toString()))
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