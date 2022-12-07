package com.example.mediaplayer.DB


class Playlist() {

    var id: Int?=null
    var playlistName: String?=null
    var playlistElements: ArrayList<Int> = ArrayList()


    constructor(id: Int, playlistName: String, playlistElements: ArrayList<Int>): this(){
        this.id=id
        this.playlistName=playlistName
        this.playlistElements=playlistElements
    }

}