package com.example.mediaplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.viewer.*
import kotlinx.android.synthetic.main.viewer.view.*

class MainActivity : AppCompatActivity(){



    var music: AudioHandler = AudioHandler
    var video: VideoHandler = VideoHandler
    var provona: Prova = Prova

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer)

        provona.provino = viewer.findViewById(R.id.bottom_navigation) as BottomNavigationView

        if (video.videoPlayer == null) {
            video.videoPlayer = ExoPlayer.Builder(this).build()
        }


        when (provona.pageDisplay) {
            0 -> {
                loadFragment(FragmentMain.newInstance())
            }
            1 -> {
                loadFragment(FragmentPlayer.newInstance())
            }
            2 -> {
                loadFragment(FragmentVideo.newInstance())
            }
        }
        //loadFragment(FragmentMain.newInstance())
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var fragment : Fragment
            Log.d("${provona.pageDisplay}", "iagvdiuagd")

            when (item.itemId){

                R.id.home -> {
                    provona.pageDisplay = 0
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)            //Permette la libera rotazione dello schermo del dispositivo all'interno del fragmentMain
                    fragment = FragmentMain()
                    loadFragment(fragment)
                    true
                }

                R.id.play -> {
                    provona.pageDisplay = 1
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR)           //Non permette la rotazione dello schermo del dispositivo all'interno del fragmentPlayer
                    fragment = FragmentPlayer()
                    loadFragment(fragment)
                    true
                }
                R.id.video -> {
                    provona.pageDisplay = 2
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)            //Permette la libera rotazione dello schermo del dispositivo all'interno del fragmentVideo
                    fragment = FragmentVideo()
                    loadFragment(fragment)
                    viewer.bottom_navigation.visibility = BottomNavigationView.GONE
                    true
                }
                else -> {
                    false
                }

            }
        }

    } // fine OnCreate



    fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.viewer, fragment)
            .commit()
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
}