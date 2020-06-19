package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

    private var displayWidth = -1
    private var displayHeight = -1

    private var visibleLeft = 0f
    private var visibleTop = 0f
    private var actualLeft = 0f
    private var actualTop = 0f
    private var isDragFinished = false

    private var previousEventX = 0f
    private var previousEventY = 0f

    private val paint = Paint()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//        (layoutParams as LayoutParams).gravity = Gravity.CENTER
        setBackgroundColor(Color.argb(125, 200, 25, 11))
        translationX = 0f
        translationY = 0f
        setBackgroundColor(Color.TRANSPARENT)
//        post { layoutParams = LayoutParams(MaxViewDrawingInt.MAX_WIDTH, MaxViewDrawingInt.MAX_HEIGHT) }
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("manideep", "onDraw")
        drawBoundaries(canvas)
    }

    private fun drawBoundaries(canvas: Canvas) {
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, paint)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawLine(width.toFloat(), height.toFloat(), 0f, height.toFloat(), paint)
        canvas.drawLine(0f, height.toFloat(), 0f, 0f, paint)
        canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (displayWidth == -1 && displayHeight == -1) {
            displayWidth = width
            displayHeight = height
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
        translationY += diffY
    }

    private fun handleActionUp() {
        if (translationX > 0) {
            translationX = 0f
        } else if (translationX + width < displayWidth) {
            translationX = displayWidth - width.toFloat()
        }
        if (translationY > 0) {
            translationY = 0f
        } else if (translationY + height < displayHeight) {
            translationY = displayHeight - height.toFloat()
        }
    }


    object MaxViewDrawingInt {
        internal const val MAX_WIDTH = 16777215
        internal const val MAX_HEIGHT = 16777215
    }


}