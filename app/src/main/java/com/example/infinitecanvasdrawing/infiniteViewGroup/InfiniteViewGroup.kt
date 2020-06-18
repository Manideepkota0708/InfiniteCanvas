package com.example.infinitecanvasdrawing.infiniteViewGroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.graphics.transform

class InfiniteViewGroup(context: Context) : View(context) {

    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels

    private var visibleLeft = 0f
    private var visibleTop = 0f
    private var actualLeft = 0f
    private var actualTop = 0f
    private var isDragFinished = false

    private var previousEventX = 0f
    private var previousEventY = 0f

    private val paint = Paint()
    private val listOfRect = arrayListOf<RectF>()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = FrameLayout.LayoutParams((Int.MAX_VALUE).toInt(), (Int.MAX_VALUE).toInt())
        (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        listOfRect.add(RectF(0f, 0f, Int.MAX_VALUE/1f, Int.MAX_VALUE/1f))
        translationX = 0f
        translationY = 0f
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("manideep", "onDraw")
        drawSquareBoundaries(canvas, this.listOfRect)
    }

    private fun drawSquareBoundaries(canvas: Canvas, listOfRectF: List<RectF>) {
        listOfRectF.forEach {
            canvas.drawRect(it, paint)
            canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }


    private fun updateLayoutParams() {

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("manideep", "onTouchEvent")
        val diffX = event.rawX - previousEventX
        val diffY = event.rawY - previousEventY
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                Log.d("manideep", " ActionMove : ${event.actionIndex}")
                handleActionMove(diffX, diffY)
            }

            MotionEvent.ACTION_UP -> {
                Log.d("manideep", "ActionUp: ${event.actionIndex}")
                handleActionUp()
            }
        }
        previousEventX = event.rawX
        previousEventY = event.rawY
        return true
    }

    private fun handleActionMove(diffX: Float, diffY: Float) {
        translationX += diffX
        visibleLeft -= diffX

        translationY += diffY
        visibleTop -= diffY
    }

    private fun handleActionUp() {
        if (visibleLeft < actualLeft) {
            visibleLeft = 0f
            actualLeft = 0f
            val matrix = matrix
        }

        if (visibleTop < actualTop) {
            visibleTop = 0f
            actualTop = 0f

        }
    }


}