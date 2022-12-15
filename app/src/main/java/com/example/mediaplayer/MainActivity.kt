package com.example.mediaplayer


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.viewer.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer)

        loadFragment(FragmentMain.newInstance())
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            var fragment : Fragment

            when (item.itemId){

                R.id.home -> {
                    fragment = FragmentMain()
                    loadFragment(fragment)
                    true
                }

                R.id.play -> {
                    fragment = FragmentPlayer()
                    loadFragment(fragment)
                    true
                }
                else -> {
                    false
                }

            }
        }

        /*val bottone: Button = findViewById(R.id.aaa)
        bottone.setOnClickListener() {
            requestRuntimePermission()
        }*/
        /*prova.setOnClickListener(){
            val intent: Intent = Intent(this, SassariMusic::class.java)
            startActivity(intent)
        }*/
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