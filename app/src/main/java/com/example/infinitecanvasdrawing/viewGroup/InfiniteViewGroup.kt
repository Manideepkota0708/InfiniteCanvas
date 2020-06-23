package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

    private var displayWidth = -1
    private var displayHeight = -1


    private var visibleLeft = 0f
    private var visibleTop = 0f
    private var visibleWidth = 0f
    private var visibleHeight = 0f

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
                visibleLeft += diffX
                visibleTop += diffY
                Log.d("manideep","transX: $diffX, transY: $diffY")
            }

            override fun performActionUp() {

                val left = translationX + (1 - scaleX) * pivotX
                val top = translationY + (1 - scaleY) * pivotY
                val width = width * scaleX
                val height = height * scaleY
                val right = left + width
                val bottom = top + height
                if (width <= displayWidth) {
                    translationX = (scaleX - 1) * pivotX + (displayWidth - width) / 2
                } else {
                    if (left > 0) {
                        translationX = (scaleX - 1) * pivotX
                    } else if (right < displayWidth) {
                        translationX = displayWidth + (scaleX - 1) * pivotX - width
                    }
                }

                if (height <= displayHeight) {
                    translationY = (scaleY - 1) * pivotY + (displayHeight - height) / 2
                } else {
                    if (top > 0) {
                        translationY = (scaleY - 1) * pivotY
                    } else if (bottom < displayHeight) {
                        translationY = displayHeight + (scaleY - 1) * pivotY - height
                    }
                }
            }

            override fun performActionZoom(
                scaleX: Float,
                scaleY: Float,
                pivotX: Float,
                pivotY: Float
            ) {
                this@InfiniteViewGroup.scaleX = scaleX
                this@InfiniteViewGroup.scaleY = scaleY
//                this@InfiniteViewGroup.pivotX = 0f
//                this@InfiniteViewGroup.pivotY = 0f
                Log.d(
                    "manideep",
                    "transX: $translationX, transY: $translationY, pivotX: ${this@InfiniteViewGroup.pivotX}, pivotY: ${this@InfiniteViewGroup.pivotY} "
                )
                Log.d("manideep", "left: $left, top: $top, width: $width, height: $height")
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