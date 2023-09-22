package com.example.basket.entity

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


class CustomTriangleShape(val direction: Direcions, ): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        var xy1 = 0f to 0f
        var xy2 = 0f to 0f
        var xy3 = 0f to 0f
        val sizeGround = 0.3f

        val path = Path().apply {
            when(direction) {
                Direcions.Top -> {
                    xy1 = size.width * sizeGround to size.height
                    xy2 = size.width/2 to 0f
                    xy3 = size.width  * (1 - sizeGround) to size.height
                }
                Direcions.Left -> {
                    xy1 = 0f to size.height / 2
                    xy2 = size.width to size.height * sizeGround
                    xy3 = size.width to size.height * (1 - sizeGround)
                }
                Direcions.Bottom -> {
                    xy1 = size.width * sizeGround to 0f
                    xy2 = size.width  * (1 - sizeGround) to 0f
                    xy3 = size.width/2 to size.height
                }
                Direcions.Right -> {
                    xy1 = 0f to size.height * (1 - sizeGround)
                    xy2 = 0f to size.height * sizeGround
                    xy3 = size.width to size.height / 2
                }
            }
            moveTo(xy1.first,xy1.second)
            lineTo(xy2.first,xy2.second)
            lineTo(xy3.first,xy3.second)
            close()
        }
        return Outline.Generic(path)
    }
}
/*
         0,0   x2,y2    width,0
          |      2      |
          |      /\     |
          |     /  \    |
          |    /    \   |
          |   /      \  |
          |  /        \ |
0,height  1 ----------  3 width,height
       x1,y1     x3,y3


            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)

 */