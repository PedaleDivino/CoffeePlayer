package com.example.mediaplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment



class FragmentPlayer : Fragment() {

    var disc: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var playerPlay : View = inflater.inflate(R.layout.player_play, container, false)
        disc = playerPlay.findViewById(R.id.albumImageId)
        discAnimation()
        return playerPlay

    }

    companion object {
        fun newInstance() = FragmentPlayer()
    }





    fun discAnimation() {

        val rotate = AnimationUtils.loadAnimation(activity,R.anim.rotation_disc)
        rotate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                val continueRotate = AnimationUtils.loadAnimation(activity, R.anim.infinite_rotation_disc)
                continueRotate.setInterpolator(LinearInterpolator())
                disc?.animation = continueRotate
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        disc?.animation = rotate

    }

}