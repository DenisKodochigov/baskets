package com.example.basket.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.basket.entity.TypeText
import com.example.basket.ui.theme.styleApp

@Composable
fun CollapsingToolbar(text: String, idImage:Int, scrollOffset: Int) {

    val heightHeader = 340.dp
    val so = (1.0 - scrollOffset/heightHeader.value.toDouble()).toFloat()

    val imageSize by animateDpAsState(targetValue = max(0.dp,heightHeader * so), label = "")
    Column {
        Image(
            painter =painterResource(id = idImage ),
            modifier = Modifier.height(imageSize),
            contentScale = ContentScale.Crop,
            contentDescription = "Photo"
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp))
        {
            TextApp(
                text = text,
                style = styleApp(nameStyle = TypeText.NAME_SCREEN),
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            )
        }
    }
}
