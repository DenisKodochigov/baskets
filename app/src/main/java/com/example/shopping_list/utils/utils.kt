package com.example.shopping_list.utils

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.SortingBy

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
            SortingBy.BY_NAME -> doubleList.add(articles.sortedBy { it.position }.toList())
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

    val direction = dismissState.dismissDirection ?: return@DismissBackground
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