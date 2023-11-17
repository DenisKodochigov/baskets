package com.example.basket.ui.screens.test

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.basket.navigation.ScreenDestination
import com.example.basket.utils.itemSwipe
import com.example.basket.utils.log
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun TestScreen(screen: ScreenDestination)
{
    TestScreenCreateView()
}
@Composable fun TestScreenCreateView(){

    val listState = rememberLazyListState()

    LazyColumn(state=listState){
        items(count = 20){item->
            itemSwipe(
                frontFon = { frontFon(item = item.toString())},
                actionDragRight = { log(true, "Run function edit")},
                actionDragLeft = { log(true, " Run function delete")},
                iconLeft = Icons.Default.Sailing,
                iconRight = Icons.Default.ArrowCircleRight
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}




@Composable fun frontFon(item: String){
    Row ( modifier = Modifier
        .background(color = Color.White)
        .fillMaxWidth()) {
        Text(text = " AAAAA")
        Spacer(modifier = Modifier
            .width(12.dp)
            .height(24.dp))
        Text(text = "$item BBBBBBBBBBBBB")
    }
}

//LazyColumn(state=listState){
//    items(count = 20){item->
//        val offsetX = remember { mutableFloatStateOf(0f) }
//        val offsetY = remember { mutableFloatStateOf(0f) }
//        var width by remember { mutableFloatStateOf(0f) }
//        Box(modifier = Modifier
//            .fillMaxWidth()
//            .onSizeChanged { width = it.width.toFloat() }){
//            Row(
//                Modifier
//                    .offset {
//                        IntOffset(
//                            offsetX.value.roundToInt(),
//                            offsetY.value.roundToInt()
//                        )
//                    }
//                    .fillMaxHeight()
//                    .fillMaxWidth()
//                    .background(Color.LightGray)
//                    .pointerInput(Unit) {
//                        awaitEachGesture {
//                            val down = awaitFirstDown()
//                            var change = awaitHorizontalTouchSlopOrCancellation(down.id)
//                            { change, over ->
//                                val originalX = offsetX.value
//                                val newValue =
//                                    (originalX + over).coerceIn(0f, width - 50.dp.toPx())
//                                change.consume()
//                                offsetX.value = newValue
//                            }
//                            while (change != null && change.pressed) {
//                                change = awaitHorizontalDragOrCancellation(change.id)
//                                if (change != null && change.pressed) {
//                                    val originalX = offsetX.value
//                                    val newValue = (originalX + change.positionChange().x)
//                                        .coerceIn(0f, width - 50.dp.toPx())
//                                    change.consume()
//                                    offsetX.value = newValue
//                                }
//                            }
//                        }
//                    }
//            ){


//val offsetX = remember { mutableStateOf(0f) }
//val offsetY = remember { mutableStateOf(0f) }
//var size by remember { mutableStateOf(Size.Zero) }
//Box(
//Modifier.fillMaxSize()
//.onSizeChanged { size = it.toSize() }
//) {
//    Box(
//        Modifier.offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
//            .size(50.dp)
//            .background(Color.Blue)
//            .pointerInput(Unit) {
//                awaitEachGesture {
//                    val down = awaitFirstDown()
//                    var change = awaitTouchSlopOrCancellation(down.id) { change, over ->
//                        val original = Offset(offsetX.value, offsetY.value)
//                        val summed = original + over
//                        val newValue = Offset(
//                            x = summed.x.coerceIn(0f, size.width - 50.dp.toPx()),
//                            y = summed.y.coerceIn(0f, size.height - 50.dp.toPx())
//                        )
//                        change.consume()
//                        offsetX.value = newValue.x
//                        offsetY.value = newValue.y
//                    }
//                    while (change != null && change.pressed) {
//                        change = awaitDragOrCancellation(change.id)
//                        if (change != null && change.pressed) {
//                            val original = Offset(offsetX.value, offsetY.value)
//                            val summed = original + change.positionChange()
//                            val newValue = Offset(
//                                x = summed.x.coerceIn(0f, size.width - 50.dp.toPx()),
//                                y = summed.y.coerceIn(0f, size.height - 50.dp.toPx())
//                            )
//                            change.consume()
//                            offsetX.value = newValue.x
//                            offsetY.value = newValue.y
//                        }
//                    }
//                }
//            }
//    )
//}