package com.call2owner.utils.animation

import android.view.animation.Interpolator
import kotlin.math.cos
import kotlin.math.pow

class MyBounceInterpolator(private val freq: Double, private val amp: Double) : Interpolator {
    override fun getInterpolation(time: Float): Float {
        return (-1 * Math.E.pow(-time / amp) * cos(freq * time) + 1).toFloat()
    }
}