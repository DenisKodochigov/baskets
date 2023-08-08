package com.example.shopping_list.entity

import kotlin.math.absoluteValue
import kotlin.math.pow

data class Wave(
    val wave: Int,
    var X: Double = 0.0,
    var Y: Double = 0.0,
    var Z: Double = 0.0,
    var R: Int = 0,
    var G: Int = 0,
    var B: Int = 0,
    var color: Long = 0L,
    var alpha: Int = 255,
    var intensity: Int = 255,
    var factor: Double = 0.0,
    var gamma: Double = 0.8
) {

    private fun abs(parameter: Double): Double = 255 *
        if (parameter.absoluteValue < 0.0031308) 12.92 * parameter
        else 1.055 * parameter.pow(0.41666) - 0.055
}


