package com.example.basket.ui.screens.article

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.basket.entity.Article
import com.example.basket.entity.Section
import com.example.basket.entity.SortingBy
import com.example.basket.entity.UnitApp

data class ArticleScreenState(
    val articles: MutableState<List<List<Article>>> = mutableStateOf(emptyList()),
    val sections: MutableState<List<Section>> = mutableStateOf(emptyList()),
    val unitApp: MutableState<List<UnitApp>> = mutableStateOf(emptyList()),
    var triggerRunOnClickFAB: MutableState<Boolean> = mutableStateOf(false),
    val editArticle: MutableState<Article?> = mutableStateOf(null),
    var onAddArticle: (Article) -> Unit = {},
    var changeArticle: (Article) -> Unit = {},
    var doChangeSectionSelected: (List<Article>, Long) -> Unit = {_,_->},
    var doDeleteSelected: (List<Article>) -> Unit = {},
    var doSelected: (Long) -> Unit = {},
    var doUnSelected: () -> Unit = {},

    var idImage: Int = 0,
    var screenTextHeader: String = "",
    val sorting: SortingBy = SortingBy.BY_NAME,
    var doChangeSortingBy: (SortingBy) -> Unit = {},
)
