package com.example.mediaplayer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.player_play.*
import kotlinx.android.synthetic.main.viewer.*
import kotlinx.android.synthetic.main.viewer.bottom_navigation


class PlayerActivity : AppCompatActivity() {

    var disc: ImageView? = null
    var musicPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_play)

        val extras = intent.extras
        var extraUri : String= ""
        var extraName : String = ""
        if (extras != null) {
            extraUri = extras.getString("Path").toString()
            extraName = extras.getString("Name").toString()

            //POSIZIONE TEMPORANEA
            val uri: Uri =  Uri.parse(extraUri)
            trackNameId.text = extraName

            musicPlayer = MediaPlayer.create(this, uri)
            musicPlayer.start()
        }
        // Inflate the layout for this fragment





        disc = findViewById(R.id.albumImageId)
        discAnimation()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            //startActivity(Intent(this, FragmentMain::class.java))

            when (item.itemId){

                R.id.home -> {
                    musicPlayer.stop()
                    musicPlayer.release()
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }

                R.id.play -> {
                    //startActivity(Intent(this, PlayerActivity::class.java))
                    true
                }
                else -> {
                    false
                }

            }
        }

    }

    fun discAnimation() {

        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val continueRotate = AnimationUtils.loadAnimation(this@PlayerActivity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }


}