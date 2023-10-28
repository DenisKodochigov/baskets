package com.example.basket.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.basket.ui.theme.massColor

@Composable fun SelectColor( doSelectedColor: (Int) -> Unit) {

    massColor.forEach { list ->
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.horizontalScroll(ScrollState(0))) {
            list.forEach { item ->
                Spacer(modifier = Modifier.width(2.dp))
                Spacer(modifier = Modifier
                    .size(size = 35.dp)
                    .clip(shape = CircleShape)
                    .background(color = item, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
                    .clickable { doSelectedColor( downIntensityColor(item)) }
                )
            }
        }
    }
}
fun downIntensityColor(item: Color): Int{
//    val x = 50f
//    return Color(red = (item.red + (1f - item.red) * (1f - x / 255)) ,
//        green = (item.green+ (1f - item.green) * (1f - x / 255)) ,
//        blue = (item.blue+ (1f - item.blue) * (1f - x / 255)),
//        alpha = 1f,
//        colorSpace = item.colorSpace).toArgb()
    return item.toArgb()
}