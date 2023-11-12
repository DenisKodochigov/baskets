package com.example.basket.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.basket.utils.log
import kotlin.math.atan

const val strokeWidth = 10f
const val  deltaYver = 4f
const val deltaXver = 3f
const val  deltaYhor = 3f
const val  deltaXhor = 4f

@Composable fun ArrowLeft(){

    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(40.dp).width(10.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = deltaXhor, y = canvasHeight/2 + deltaYhor ),
            end = Offset(x = canvasWidth - deltaXhor, y = 0f + deltaYhor),)
        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = deltaXhor, y = canvasHeight/2  - deltaYhor),
            end = Offset(x = canvasWidth - deltaXhor, y = canvasHeight - deltaYhor),)
    }
}

@Composable fun ArrowRight(){

    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(40.dp).width(10.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = canvasWidth - deltaXhor, y = canvasHeight/2 + deltaYhor ),
            end = Offset(x = deltaXhor, y = 0f + deltaYhor),)
        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = canvasWidth - deltaXhor, y = canvasHeight/2  - deltaYhor),
            end = Offset(x = deltaXhor, y = canvasHeight - deltaYhor),)
    }
}
@Composable fun ArrowUp(){

    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(10.dp).width(40.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = 0f + deltaXver, y = canvasHeight - deltaYver ),
            end = Offset(x = canvasWidth/2 + deltaXver, y = 0f + deltaYver),)

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = canvasWidth/2 - deltaXver, y = 0f + deltaYver),
            end = Offset(x = canvasWidth - deltaXver, y = canvasHeight - deltaYver),)
    }
}
@Composable fun ArrowDown(){

    val colorLine = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.height(10.dp).width(40.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = 0f + deltaXver , y = 0f + deltaYver),
            end = Offset(x = canvasWidth/2 + deltaXver, y = canvasHeight - deltaYver),)
        drawLine(
            color = colorLine,
            strokeWidth = strokeWidth,
            start = Offset(x = canvasWidth/2 - deltaXver, y = canvasHeight - deltaYver ),
            end = Offset(x =  canvasWidth - deltaXver, y = 0f + deltaYver),)
    }
}
@Composable fun ArrowNone(){
    Canvas(modifier = Modifier.height(10.dp).width(40.dp)) {}
}
@Preview
@Composable fun ArrowLeftPreview(){
    ArrowDown()
}