package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout

class EditableViewGroup : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val paint = Paint()

    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 50f
        paint.isAntiAlias = true
        paint.isDither = true
        translationX = 0f
        translationY = 0f
        setBackgroundColor(Color.YELLOW)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {

        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}