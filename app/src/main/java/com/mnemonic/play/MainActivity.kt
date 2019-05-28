package com.mnemonic.play

,

import android.graphics.drawable.Drawable

,
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    var imageList: List<Int> = arrayListOf(
        R.drawable.ic_beach,
        R.drawable.ic_audio,
        R.drawable.ic_dollar,
        R.drawable.ic_car,
        R.drawable.ic_cloud,
        R.drawable.ic_camera,
        R.drawable.ic_android,
        R.drawable.ic_bluetooth,
        R.drawable.ic_cake,
        R.drawable.ic_bulb,
        R.drawable.ic_drink,
        R.drawable.ic_food,
        R.drawable.ic_flower,
        R.drawable.ic_heart,
        R.drawable.ic_hot,
        R.drawable.ic_location,
        R.drawable.ic_laptop,
        R.drawable.ic_paw,
        R.drawable.ic_lock,
        R.drawable.ic_sofa,
        R.drawable.ic_plane,
        R.drawable.ic_train,
        R.drawable.ic_star,
        R.drawable.ic_world
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
