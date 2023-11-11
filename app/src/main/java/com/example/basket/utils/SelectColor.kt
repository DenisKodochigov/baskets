package com.example.basket.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.entity.UPDOWN
import com.example.basket.ui.components.showArrowHor
import com.example.basket.ui.components.showArrowVer
import com.example.basket.ui.theme.massColor

@Composable fun SelectColor( doSelectedColor: (Int) -> Unit) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .verticalScroll(ScrollState(0))) {

        massColor.forEach { list ->
            val lazyState = rememberLazyListState()
            val showArrowStart = remember {
                derivedStateOf { lazyState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != 0 }
            }.value
            val showArrowEnd = remember {
                derivedStateOf { lazyState.layoutInfo.visibleItemsInfo.lastOrNull()?.index !=
                        lazyState.layoutInfo.totalItemsCount - 1 } }.value
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                showArrowHor(direction = UPDOWN.START, enable = showArrowStart && list.isNotEmpty(), drawLine = false)
                LazyRow(state = lazyState, modifier = Modifier.weight(1f)){
                    items(list){item ->
                        Spacer(modifier = Modifier.width(2.dp))
                        Spacer(modifier = Modifier
                            .size(size = 35.dp)
                            .clip(shape = CircleShape)
                            .background(color = item, shape = CircleShape)
                            .clickable { doSelectedColor(downIntensityColor(item)) }
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                        )
                    }
                }
                showArrowHor(direction = UPDOWN.END, enable = showArrowEnd && list.isNotEmpty(), drawLine = false)
            }
        Spacer(Modifier.height(12.dp))
    }


//        Row(modifier = Modifier.horizontalScroll(ScrollState(0))) {
//            list.forEach { item ->
//                Spacer(modifier = Modifier.width(2.dp))
//                Spacer(modifier = Modifier
//                    .size(size = 35.dp)
//                    .clip(shape = CircleShape)
//                    .background(color = item, shape = CircleShape)
//                    .border(
//                        width = 1.dp,
//                        color = MaterialTheme.colorScheme.outline,
//                        shape = CircleShape
//                    )
//                    .clickable { doSelectedColor(downIntensityColor(item)) }
//                )
//            }
//        }
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