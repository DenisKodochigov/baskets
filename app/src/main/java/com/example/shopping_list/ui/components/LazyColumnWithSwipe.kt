package com.example.shopping_list.ui.components

import android.os.Looper
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Product

data class DataSet(
    val itemId: Int,
    val itemName: String,
    val itemQty: String
)

private val ListComparator = Comparator<DataSet> { left, right ->
    left.itemId.compareTo(right.itemId)
}

@Composable
fun ItemRow(
    modifier: Modifier = Modifier,
    product: DataSet,
    number: Int) {
    Card( shape = RoundedCornerShape(4.dp),
        modifier = modifier.padding(8.dp).fillMaxWidth(),
        backgroundColor = Color.LightGray) {
        Row(modifier = modifier) {
            Text(text = "$number.", modifier = Modifier.weight(2f).padding(start = 8.dp, end = 4.dp))
            Text(text = product.itemName, modifier = Modifier.weight(10f).padding(end = 4.dp))
            Text(text = product.itemQty, modifier = Modifier.weight(2f).padding(end = 4.dp))
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun LazyColumnWithSwipe() {

    var list by remember { mutableStateOf(listOf<DataSet>()) }
    val comparator by remember { mutableStateOf(ListComparator) }

    LazyColumn {
        item {
            Button(onClick = { list = list + listOf(DataSet((0..1111).random(), "A random item", "100")) })
                { Text("Add an item to the list") }
        }
        val sortedList = list.sortedWith(comparator)
        items(items = sortedList, key = { it.itemId }) { item ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                        android.os.Handler(Looper.getMainLooper()).postDelayed({ /*dataList.remove(item)*/ }, 1000) }
                    true }
            )
            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                list = list.toMutableList().also { it.remove(item) } // remove
            }
            SwipeToDismiss(state = dismissState,
                modifier = Modifier.padding(vertical = 1.dp).animateItemPlacement(),
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold( if (direction == DismissDirection.StartToEnd) 0.1f else 1.1f) },
                background = {
                    val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState( when (dismissState.targetValue) {
                            DismissValue.Default -> Color.LightGray
                            DismissValue.DismissedToEnd -> Color.Green
                            DismissValue.DismissedToStart -> Color.Red
                    })
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Default.Done
                        DismissDirection.EndToStart -> Icons.Default.Delete }
                    val scale by animateFloatAsState( if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f)
                    Box(Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp), contentAlignment = alignment) {
                        Icon(icon, contentDescription = "Localized description", modifier = Modifier.scale(scale)) }
//                    if (direction == DismissDirection.StartToEnd) {
//                        Box(modifier = Modifier.fillMaxSize().background(color).padding(8.dp)) {
//                            Column(modifier = Modifier.align(Alignment.CenterStart)) {
//                                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
//                                Text(text = "Move to Archive", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.White)
//                            }
//                        }
//                    } else {
//                        Box(modifier = Modifier.fillMaxSize().background(color).padding(8.dp)) {
//                            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
//                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
//                                Spacer(modifier = Modifier.heightIn(5.dp))
//                                Text(text = "Move to Bin", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.LightGray)
//                            }
//                        }
//                    }
                },
                dismissContent = {
                    Card(elevation = animateDpAsState(if (dismissState.dismissDirection != null) 4.dp else 4.dp).value) {
                        ItemRow(product = item, number = item.itemId) }
                }
            )
        }
    }
}

@Composable
private fun adminCard() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Card(shape = RoundedCornerShape(14.dp), backgroundColor = Color.White, modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.ic_baseline_download_24),
                    contentDescription = null, modifier = Modifier.size(65.dp),)
                Row(modifier = Modifier.padding(start = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Admin", style = TextStyle(color = Color.Gray, fontSize = 16.sp, textAlign = TextAlign.Center))
                        Text(text = "This is admin information", style = TextStyle(color = Color.Red, fontSize = 16.sp, textAlign = TextAlign.Center))
                    }
                }
            }
        }
    }
}

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun <T> AnimatedSwipeDismiss(
    modifier: Modifier = Modifier,
    item: T,
    background: @Composable (isDismissed: Boolean) -> Unit,
    content: @Composable (isDismissed: Boolean) -> Unit,
    directions: Set<DismissDirection> = setOf(DismissDirection.EndToStart),
    enter: EnterTransition = expandVertically(),
    exit: ExitTransition = shrinkVertically(animationSpec = tween(durationMillis = 500,)),
    onDismiss: (T) -> Unit
) {
    val dismissState = rememberDismissState()
    val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
    if (isDismissed) { onDismiss(item) }
    AnimatedVisibility(modifier = modifier, visible = !isDismissed, enter = enter, exit = exit
    ) {
        SwipeToDismiss(modifier = modifier, state = dismissState, directions = directions,
            background = { background(isDismissed) }, dismissContent = { content(isDismissed) })
    }
}