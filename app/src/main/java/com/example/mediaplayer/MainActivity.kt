package com.example.mediaplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var disc: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //disc = findViewById<ImageView>(R.id.albumImageId)
        //discAnimation()
        val bottone : Button = findViewById(R.id.aaa)
        bottone.setOnClickListener(){
            requestRuntimePermission()
        }
        prova.setOnClickListener(){
            val intent: Intent = Intent(this, SassariMusic::class.java)
            startActivity(intent)
        }
    }



    fun requestRuntimePermission() : Boolean {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), 113)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 113)
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
    }















    fun discAnimation() {

        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotation_disc)
        rotate.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val continueRotate = AnimationUtils.loadAnimation(this@MainActivity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }

}