package com.example.infinitecanvasdrawing.viewGroup

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.infinitecanvasdrawing.utils.MathUtil

internal class GestureEditingViewGroup(
    context: Context,
    private val gestureEditingListener: GestureEditingListener?
) : FrameLayout(context) {
    constructor(context: Context) : this(context, null)

    private lateinit var editMode: EditMode


    private var previousEventX = 0f
    private var previousEventY = 0f
    private var previousScale = 1f
    private var previousRenderedScale = 1f
    private var initialZoomDistance = 1f


    init {
//        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//        setBackgroundColor(Color.MAGENTA)
//        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                previousEventX = event.x
                previousEventY = event.y
                editMode = EditMode.DRAG
            }
            MotionEvent.ACTION_MOVE -> {
                when (editMode) {
                    EditMode.DRAG -> {
                        val diffX = event.x - previousEventX
                        val diffY = event.y - previousEventY
                        gestureEditingListener?.performTranslation(diffX, diffY)
                        previousEventX = event.x
                        previousEventY = event.y
                    }
                    EditMode.ZOOM -> {
                        // TODO
                        val firstPoint = PointF(event.getX(0), event.getY(0))
                        val secondPoint = PointF(event.getX(1), event.getY(1))
                        val midPoint = MathUtil.getMidPoint(firstPoint, secondPoint)
                        val newDistance = MathUtil.distanceBetweenPoints(
                            firstPoint.x,
                            firstPoint.y,
                            secondPoint.x,
                            secondPoint.y
                        )
                        val newScale = (newDistance / initialZoomDistance) * previousRenderedScale
//                        Log.d("manideep", "p1: $firstPoint, p2: $secondPoint, newScale: $newScale")
                        if (newScale != previousScale) {
                            gestureEditingListener?.performActionZoom(
                                newScale,
                                newScale,
                                midPoint.x,
                                midPoint.y
                            )
                            previousScale = newScale
                        }
                    }
                }

            }

            MotionEvent.ACTION_UP -> {
                gestureEditingListener?.performActionUp()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                editMode = EditMode.ZOOM
                if (event.pointerCount == 2) {
                    initialZoomDistance = MathUtil.distanceBetweenPoints(
                        event.getX(0),
                        event.getY(0),
                        event.getX(1),
                        event.getY(1)
                    )
                    previousScale = 1f
                }

            }

            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount == 2) {
                    editMode = EditMode.DRAG
                    val actionIndex = event.actionIndex
                    previousEventX = if (actionIndex == 0) event.getX(1) else event.x
                    previousEventY = if (actionIndex == 0) event.getY(1) else event.y
                    previousRenderedScale = previousScale

                }
            }

        }
        return true
    }
}

private enum class EditMode {
    DRAG, ZOOM
}

internal interface GestureEditingListener {
    fun performTranslation(diffX: Float, diffY: Float)

    fun performActionUp()

    fun performActionZoom(newScaleX: Float, newScaleY: Float, newPivotX: Float, newPivotY: Float)
}