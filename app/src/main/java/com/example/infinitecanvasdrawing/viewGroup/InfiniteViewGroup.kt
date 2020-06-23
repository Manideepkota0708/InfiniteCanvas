package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

    private var displayWidth = -1
    private var displayHeight = -1

    private val paint = Paint()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 500000f
        paint.isAntiAlias = true
        paint.isDither = true
        setBackgroundColor(Color.TRANSPARENT)
        val gestureEditingListener = object : GestureEditingListener {
            override fun performTranslation(diffX: Float, diffY: Float) {
                translationX += diffX
                translationY += diffY
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
                newScaleX: Float,
                newScaleY: Float,
                newPivotX: Float,
                newPivotY: Float
            ) {

                val oldTranslationX = translationX + (1 - scaleX) * pivotX
                val oldTranslationY = translationY + (1 - scaleY) * pivotY

                val tempPivotX = newPivotX - oldTranslationX
                val tempPivotY = newPivotY - oldTranslationY

                val newTranslationX = (1 - newScaleX / scaleX) * tempPivotX
                val newTranslationY = (1 - newScaleY / scaleY) * tempPivotY

                val finalTranslationX = oldTranslationX + newTranslationX
                val finalTranslationY = oldTranslationY + newTranslationY

                val finalPivotX = (finalTranslationX - translationX) / (1 - newScaleX)
                val finalPivotY = (finalTranslationY - translationY) / (1 - newScaleY)

                scaleX = newScaleX
                scaleY = newScaleY
                pivotX = finalPivotX
                pivotY = finalPivotY
                Log.d(
                    "manideep",
                    "transX: $translationX, transY: $translationY, pivotX: ${this@InfiniteViewGroup.pivotX}, pivotY: ${this@InfiniteViewGroup.pivotY}, scaleX: $scaleX, scaleY: $scaleY "
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
            layoutParams = LayoutParams(MaxViewDrawingInt.MAX_WIDTH , MaxViewDrawingInt.MAX_HEIGHT)
            layoutParams = LayoutParams(MaxViewDrawingInt.MAX_WIDTH, MaxViewDrawingInt.MAX_HEIGHT)
            translationX = (displayWidth - MaxViewDrawingInt.MAX_WIDTH) / 2f
            translationY = (displayHeight - MaxViewDrawingInt.MAX_WIDTH) / 2f
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
        canvas.drawPoint(width / 2f, height / 3f, paint)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (displayWidth == -1 && displayHeight == -1) {
            displayWidth = width
            displayHeight = height
        }
    }

    object MaxViewDrawingInt {
        internal const val MAX_WIDTH = 16777215
        internal const val MAX_HEIGHT = 16777215
    }

}