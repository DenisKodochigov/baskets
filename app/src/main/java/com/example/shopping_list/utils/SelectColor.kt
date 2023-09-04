package com.example.shopping_list.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.shopping_list.ui.theme.massColor

@Composable fun SelectColor( doSelectedColor: (Int) -> Unit) {

    massColor.forEach { list ->
        Row(modifier = Modifier.horizontalScroll(ScrollState(0))) {
            list.forEach { item ->
                Spacer(modifier = Modifier
                    .size(size = 35.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
                    .clickable { doSelectedColor(item.toArgb()) }
                    .background(color = item, shape = CircleShape))
            }
        }
    }
}