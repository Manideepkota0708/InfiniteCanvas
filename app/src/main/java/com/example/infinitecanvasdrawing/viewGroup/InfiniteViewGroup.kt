package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

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
        layoutParams = LayoutParams(Int.MAX_VALUE, Int.MAX_VALUE)
//        (layoutParams as LayoutParams).gravity = Gravity.CENTER
        listOfRect.add(RectF(0f, 0f, Int.MAX_VALUE / 1f, Int.MAX_VALUE / 1f))
        translationX = 0f
        translationY = 0f
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("manideep", "onDraw")
        drawSquareBoundaries(canvas, this.listOfRect)
    }

    private fun drawSquareBoundaries(canvas: Canvas, listOfRectF: List<RectF>) {
        listOfRectF.forEach {
            val path = Path()
            path.moveTo(it.left, it.top)
            path.lineTo(it.left, it.right)
            path.lineTo(it.bottom, it.right)
            path.lineTo(it.left, it.bottom)
            path.close()
            canvas.drawPath(path, paint)
            canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }


    private fun updateLayoutParams() {

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("manideep", "onTouchEvent -> count : ${event.pointerCount}, actionIndex: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}, actionMasked: ${event.actionMasked}")
        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val diffX = event.rawX - previousEventX
                val diffY = event.rawY - previousEventY
//                Log.d("manideep", " Action_Move : ${event.actionIndex}")
                handleActionMove(diffX, diffY)
                previousEventX = event.rawX
                previousEventY = event.rawY
            }

            MotionEvent.ACTION_UP -> {
                Log.d("manideep", "Action_Up: ${event.actionIndex}")
                handleActionUp()
            }

            MotionEvent.ACTION_DOWN -> {
                Log.d("manideep", "Action_Down: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}")
                previousEventX = event.rawX
                previousEventY = event.rawY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.d("manideep", "Action_Pointer_Down: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}")
            }
        }
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