package com.example.shopping_list.entity

import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue
import kotlin.math.pow

data class Wave(
    val wave: Int,
    val X: Double,
    val Y: Double,
    val Z: Double,
    var color: Long,
    var alpha: Int = 255,
    var intensity: Int = 255
) {

    fun rgb(): String {

        val R = 3.2404542 * X - 1.5371385 * Y - 0.4985314 * Z
        val G = -0.9692660 * X + 1.8760108 * Y + 0.0415560 * Z
        val B = 0.0556434 * X - 0.2040259 * Y + 1.0572252 * Z

        val sR = if (R > 0) abs(R).toInt() else 0
        val sG = if (G > 0) abs(G).toInt() else 0
        val sB = if (B > 0) abs(B).toInt() else 0

        val t1 = "%02x".format(sR)
        val t2 = "%02x".format(sG)
        val t3 = "%02x".format(sB)
        return "0xff$t1$t2$t3"
    }

    private fun abs(parameter: Double): Double = 255 *
        if (parameter.absoluteValue < 0.0031308) 12.92 * parameter
        else 1.055 * parameter.pow(0.41666) - 0.055

    fun createListSpectrumRGB(): List<Color>{
        val listColor = mutableListOf<Color>()
        for (i in 380..780 step 5) {

        }
         return listColor
    }
}


