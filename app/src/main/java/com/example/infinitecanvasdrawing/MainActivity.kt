package com.example.infinitecanvasdrawing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.example.infinitecanvasdrawing.viewGroup.InfiniteViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editableViewGroup = findViewById<FrameLayout>(R.id.editableViewGroup)
        val infiniteViewGroup = InfiniteViewGroup(applicationContext)

        editableViewGroup.addView(infiniteViewGroup)
        Log.d("manideep","infiniteViewGroup added and child count is ${editableViewGroup.childCount}")
    }

}