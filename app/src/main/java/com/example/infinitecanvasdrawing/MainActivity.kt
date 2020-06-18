package com.example.infinitecanvasdrawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.example.infinitecanvasdrawing.infiniteViewGroup.InfiniteViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout) as FrameLayout
        val infiniteViewGroup = InfiniteViewGroup(applicationContext)
        frameLayout.addView(infiniteViewGroup)
        Log.d("manideep","infiniteViewGroup added and child count is ${findViewById<FrameLayout>(R.id.frameLayout).childCount}")
    }

}