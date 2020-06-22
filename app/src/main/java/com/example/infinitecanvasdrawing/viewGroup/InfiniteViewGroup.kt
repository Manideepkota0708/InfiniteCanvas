package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.infinitecanvasdrawing.utils.MathUtil

class InfiniteViewGroup(context: Context) : FrameLayout(context) {

    private var displayWidth = -1
    private var displayHeight = -1

    private var visibleLeft = 0f
    private var visibleTop = 0f
    private var actualLeft = 0f
    private var actualTop = 0f
    private var isDragFinished = false

    private lateinit var editMode: EditMode

    private var previousEventX = 0f
    private var previousEventY = 0f
    private var previousScale = 1f
    private var initialZoomDistance = 1f

    private val paint = Paint()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.isDither = true
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//        (layoutParams as LayoutParams).gravity = Gravity.CENTER
//        setBackgroundColor(Color.argb(125, 200, 25, 11))
        setBackgroundColor(Color.TRANSPARENT)
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

    private fun updateLayoutParams() {

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.d(
//            "manideep",
//            "onTouchEvent -> count : ${event.pointerCount}, actionIndex: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}, actionMasked: ${event.actionMasked}"
//        )
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
//                Log.d(
//                    "manideep",
//                    "Action_Down: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}"
//                )
                previousEventX = event.rawX
                previousEventY = event.rawY
                editMode = EditMode.DRAG
            }
            MotionEvent.ACTION_MOVE -> {
                when (editMode) {
                    EditMode.DRAG -> {
                        val diffX = event.rawX - previousEventX
                        val diffY = event.rawY - previousEventY
                        handleActionMove(diffX, diffY)
                        previousEventX = event.rawX
                        previousEventY = event.rawY
                    }
                    EditMode.ZOOM -> {
                        // TODO
                        val firstPoint = PointF(event.getRawX(0), event.getRawY(0))
                        val secondPoint = PointF(event.getRawX(1), event.getRawY(1))
                        handleActionZoom(firstPoint, secondPoint)
                    }
                }

            }

            MotionEvent.ACTION_UP -> {
//                Log.d("manideep", "Action_Up: ${event.actionIndex}")
                handleActionUp()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
//                Log.d(
//                    "manideep",
//                    "Action_Pointer_Down: ${event.actionIndex}, rawX: ${event.rawX}, rawY: ${event.rawY}"
//                )
                editMode = EditMode.ZOOM
                if (event.pointerCount == 2) {
                    initialZoomDistance = MathUtil.distanceBetweenPoints(event.getRawX(0), event.getRawY(0), event.getRawX(1), event.getRawY(1))
                }

            }

            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount == 2) {
                    editMode = EditMode.DRAG
                    previousEventX = event.rawX
                    previousEventY = event.rawY

                }
            }

        }
        return true
    }

    private fun handleActionMove(diffX: Float, diffY: Float) {
        translationX += diffX
        translationY += diffY
    }

    private fun handleActionZoom(p1: PointF, p2: PointF) {
        val midPoint = MathUtil.getMidPoint(p1, p2)
        val newDistance = MathUtil.distanceBetweenPoints(p1.x, p1.y, p2.x, p2.y)
        val newScale = newDistance / initialZoomDistance
        Log.d("manideep", "p1: $p1, p2: $p2, newScale: $newScale")
//        scaleX = newScale
//        scaleY = newScale

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

    private enum class EditMode {
        DRAG, ZOOM
    }

    object MaxViewDrawingInt {
        internal const val MAX_WIDTH = 16777215
        internal const val MAX_HEIGHT = 16777215
    }


}