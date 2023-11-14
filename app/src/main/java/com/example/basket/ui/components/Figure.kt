package com.example.basket.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val strokeWidth = 3f
const val arrowWidthHor = 2f
const val  deltaUpDownY = 4f
const val deltaUpDownX = 1f
const val  deltaLeftRightY = 1f
const val  deltaLeftRightX = 4f
const val  heightHor = 20f
const val  widthHor = 10f
const val  heightVer = 10f
const val  widthVer = 40f

@Composable fun ArrowLeft()
{
    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(Dp(heightHor)).width(Dp(widthHor))) {
        drawLine(
            color = colorLine,
            strokeWidth = arrowWidthHor,
            start = Offset(x = deltaLeftRightX, y = size.height/2 + deltaLeftRightY ),
            end = Offset(x = size.width - deltaLeftRightX, y = 0f + deltaLeftRightY),)
        drawLine(
            color = colorLine,
            strokeWidth = arrowWidthHor,
            start = Offset(x = deltaLeftRightX, y = size.height/2  - deltaLeftRightY),
            end = Offset(x = size.width - deltaLeftRightX, y = size.height - deltaLeftRightY),)
    }
}
@Composable fun ArrowRight()
{
    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(Dp(heightHor)).width(Dp(widthHor))) {
        drawLine(
            color = colorLine,
            strokeWidth = arrowWidthHor,
            start = Offset(x = size.width - deltaLeftRightX, y = size.height/2 + deltaLeftRightY ),
            end = Offset(x = deltaLeftRightX, y = 0f + deltaLeftRightY),)
        drawLine(
            color = colorLine,
            strokeWidth = arrowWidthHor,
            start = Offset(x = size.width - deltaLeftRightX, y = size.height/2  - deltaLeftRightY),
            end = Offset(x = deltaLeftRightX, y = size.height - deltaLeftRightY),)
    }
}
@Composable fun ArrowUp()
{
    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(Dp(heightVer)).width(Dp(widthVer))) {

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = 0f + deltaUpDownX, y = size.height - deltaUpDownY ),
            end = Offset(x = size.width/2 + deltaUpDownX, y = 0f + deltaUpDownY),)

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = size.width/2 - deltaUpDownX, y = 0f + deltaUpDownY),
            end = Offset(x = size.width - deltaUpDownX, y = size.height - deltaUpDownY),)
    }
}
@Composable fun ArrowDown()
{
    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(Dp(heightVer)).width(Dp(widthVer))) {
        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = 0f + deltaUpDownX , y = 0f + deltaUpDownY),
            end = Offset(x = size.width/2 + deltaUpDownX, y = size.height - deltaUpDownY),)
        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = size.width/2 - deltaUpDownX, y = size.height - deltaUpDownY ),
            end = Offset(x =  size.width - deltaUpDownX, y = 0f + deltaUpDownY),)
    }
}
@Composable fun ArrowNoneVer(){
    Canvas(modifier = Modifier.height(Dp(heightVer)).width(Dp(widthVer))) {}
}
@Composable fun ArrowNoneHor(){
    Canvas(modifier = Modifier.height(Dp(heightHor)).width(Dp(widthHor))) {}
}
@Preview
@Composable fun ArrowLeftPreview(){
    ArrowLeft()
}