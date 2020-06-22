package com.example.infinitecanvasdrawing.utils

import android.graphics.PointF
import kotlin.math.pow
import kotlin.math.sqrt

object MathUtil {

    fun getMidPoint(point1: PointF, point2: PointF): PointF? {
        val midPoint = PointF()
        midPoint.x = (point1.x + point2.x) / 2
        midPoint.y = (point1.y + point2.y) / 2
        return midPoint
    }

    fun distanceBetweenPoints(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }
}