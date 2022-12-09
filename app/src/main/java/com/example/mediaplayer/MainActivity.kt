package com.example.mediaplayer

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    var disc: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_play)

        disc = findViewById<ImageView>(R.id.albumImageId)
        discAnimation()
    }



























    fun discAnimation() {

        var rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        rotate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                var continueRotate = AnimationUtils.loadAnimation(this@MainActivity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }

}