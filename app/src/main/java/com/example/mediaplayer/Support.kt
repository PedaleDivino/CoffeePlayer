package com.example.mediaplayer

import com.google.android.material.bottomnavigation.BottomNavigationView

//  Una classe speciale di nome Support che può avere solo una istanza che gestisce alcune variabili di appoggio
object Support {

    var navView: BottomNavigationView ?= null
    var delBtnControl: Boolean = false
    var typeDisplay: Int = 0
    var pageDisplay: Int = 0

}