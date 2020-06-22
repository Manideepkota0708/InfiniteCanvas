package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.infinitecanvasdrawing.utils.MathUtil

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

    private var displayWidth = -1
    private var displayHeight = -1

    private var visibleLeft = 0f
    private var visibleTop = 0f
    private var actualLeft = 0f
    private var actualTop = 0f

    private val paint = Paint()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.isDither = true
//        (layoutParams as LayoutParams).gravity = Gravity.CENTER
//        setBackgroundColor(Color.argb(125, 200, 25, 11))
        setBackgroundColor(Color.TRANSPARENT)
        val gestureEditingListener = object : GestureEditingListener {
            override fun performTranslation(diffX: Float, diffY: Float) {
                translationX += diffX
                translationY += diffY
            }

            override fun performActionUp() {
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

            override fun performActionZoom(scaleX: Float, scaleY: Float, pivotX: Float, pivotY: Float) {
                this@InfiniteViewGroup.scaleX = scaleX
                this@InfiniteViewGroup.scaleY = scaleY
                Log.d("manideep", "transX: $translationX, transY: $translationY")
            }
        }
        post {
            (parent as EditableViewGroup).addView(
                GestureEditingViewGroup(
                    context,
                    gestureEditingListener
                )
            )
        }
//        post { layoutParams = LayoutParams(MaxViewDrawingInt.MAX_WIDTH, MaxViewDrawingInt.MAX_HEIGHT) }
    }

    override fun onDraw(canvas: Canvas) {
//        Log.d("manideep", "onDraw")
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

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        val x = 0f
        super.setLayoutParams(params)
    }

    object MaxViewDrawingInt {
        internal const val MAX_WIDTH = 16777215
        internal const val MAX_HEIGHT = 16777215
    }

}