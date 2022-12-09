package com.example.mediaplayer.DB


class Playlist() {

    var id: Int?=null
    var playlistName: String?=null
    var playlistElements: ArrayList<String> = ArrayList()


    constructor(id: Int, playlistName: String, playlistElements: ArrayList<String>): this(){
        this.id=id
        this.playlistName=playlistName
        this.playlistElements=playlistElements
    }

}