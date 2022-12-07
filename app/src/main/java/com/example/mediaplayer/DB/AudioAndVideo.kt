package com.example.mediaplayer.DB

class AudioAndVideo() {

    var id: Int?=null
    var fileName: String?=null
    var fileType: String?=null
    var filePath: String?=null


    constructor(id: Int, fileName: String, fileType: String, filePath: String): this(){
        this.id=id
        this.fileName=fileName
        this.fileType=fileType
        this.filePath=filePath
    }
}