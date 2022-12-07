package com.example.mediaplayer.DB

//CREAZIONE DEL DATABASE

//  DATI (necessari) E CARATTERISTICHE DEL NOSTRO DATABASER
val DATABASE_VERSION: Int = 1
val DATABASE_NAME: String = "mediaplayer.db"
val TABLE_FILE: String = "Audio_e_Video.db"
val TABLE_PLAYLIST: String = "Playlist.db"

//  NOMI COLONNE DELLA TABELLA TABLE_FILE
val KEY_FILE_ID: String = "id"
val KEY_FILE_NAME: String = "file_name"
val KEY_FILE_TYPE: String = "file_type"
val KEY_FILE_PATH: String = "file_path"

//  NOMI COLONNE DELLA TABELLA TABLE_FILE
val KEY_PLAYLIST_ID: String = "id"
val KEY_PLAYLIST_NAME: String = "playlist_name"
val KEY_PLAYLIST_TYPE: String = "file"