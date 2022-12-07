package com.example.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.coroutines.delay


class MainActivity : AppCompatActivity() {

    var disc : ImageView ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_play)

        /*
        funzione di rotazione dell'album IN RICHIAMO
         */

        disc = findViewById<ImageView>(R.id.albumImageId)
        firstRotateAnimation()

    }

























    /*
        funzione di rotazione dell'album IN RICHIAMO
    */
    fun firstRotateAnimation(){

        var rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        disc?.animation = rotate

    }

    suspend fun continueRotateAnimation(){

        delay(1000)
        var rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        disc?.animation = rotate

    }




}