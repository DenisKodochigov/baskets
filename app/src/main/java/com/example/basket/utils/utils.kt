package com.example.basket.utils

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.basket.entity.Article
import com.example.basket.entity.Product
import com.example.basket.entity.SortingBy
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun createDoubleListProduct(products: List<Product>): List<List<Product>>{
    val doubleList = mutableListOf<List<Product>>()
    val listProduct = mutableListOf<Product>()
    if (products.isNotEmpty()) {
        val productsLocal = products.sortedWith( compareBy ( {it.article.section.nameSection}
            , {it.article.nameArticle} ))
        var currentSection = productsLocal[0].article.section.idSection
        productsLocal.forEach { item->
            if (currentSection == item.article.section.idSection) listProduct.add(item)
            else {
                doubleList.add(listProduct.toList())
                currentSection = item.article.section.idSection
                listProduct.clear()
                listProduct.add(item)
            }
        }
        if (listProduct.isNotEmpty()) doubleList.add(listProduct)
    }
    return doubleList
}
fun createDoubleLisArticle(articles: List<Article>, sortingBy: SortingBy): List<List<Article>>{
    val doubleList = mutableListOf<List<Article>>()
    val listArticle = mutableListOf<Article>()
    if (articles.isNotEmpty()) {
        when(sortingBy){
            SortingBy.BY_NAME -> doubleList.add(articles.sortedBy { it.nameArticle }.toList())
            SortingBy.BY_SECTION ->{
                val articlesLocal = articles.sortedWith( compareBy ( {it.section.nameSection},
                    {it.nameArticle} ))
                var currentSection = articlesLocal[0].section.idSection
                articlesLocal.forEach{ item->
                    if (currentSection == item.section.idSection) listArticle.add(item)
                    else {
                        doubleList.add(listArticle.toList())
                        currentSection = item.section.idSection
                        listArticle.clear()
                        listArticle.add(item)
                    }
                }
                if (listArticle.isNotEmpty()) doubleList.add(listArticle)
            }
        }
    }
    return doubleList
}

fun log(showLog: Boolean, text: String){
    if (showLog)Log.d("KDS", text)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {

    val direction = dismissState.dismissDirection ?: return
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Edit
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val colorIcon = when (direction) {
        DismissDirection.StartToEnd -> Color.Green
        DismissDirection.EndToStart -> Color.Red
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp), contentAlignment = alignment) {
//        if (dismissState.progress.fraction != 1.0f)
        Icon(icon, null, tint = colorIcon)
    }
}

@Composable fun selectSectionWithArticle(id: Long, listArticle: List<Article>): Pair<Long, String> {
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) {
        Pair(article.section.idSection, article.section.nameSection)
    } else Pair(0L, "")
}

@Composable fun selectUnitWithArticle(id: Long, listArticle: List<Article>): Pair<Long, String> {
    val article = listArticle.find { it.idArticle == id }
    return if (article != null) Pair(article.unitApp.idUnit, article.unitApp.nameUnit)
    else Pair(0L, "")
}

@Composable fun ItemSwipe(
    frontFon:@Composable () -> Unit,
    actionDragRight:()->Unit,
    actionDragLeft:()->Unit,
    iconLeft: ImageVector = Icons.Default.Edit,
    iconRight: ImageVector = Icons.Default.Delete
){
    val offsetX = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val limitDraggable = 150.dp

    Box(modifier = Modifier.fillMaxWidth()
    ){
        BackFon(iconRight = iconRight,iconLeft = iconLeft)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch { offsetX.snapTo(offsetX.value + delta) }
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        coroutineScope.launch {
                            if (Dp(offsetX.value) > limitDraggable) actionDragRight()
                            if (Dp((-1) * offsetX.value) > limitDraggable) actionDragLeft()
                            offsetX.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 1000,
                                    delayMillis = 0
                                )
                            )
                        }
                    }
                )
        ) {
            frontFon()
        }
    }
}
@Composable fun BackFon(iconLeft: ImageVector, iconRight: ImageVector){
    Row(modifier = Modifier.fillMaxWidth(). padding(horizontal = 12.dp)) {
        Icon(imageVector = iconLeft, contentDescription = "")
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = iconRight, contentDescription = "")
    }
}
