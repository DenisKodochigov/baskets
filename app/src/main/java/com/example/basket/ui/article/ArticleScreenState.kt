package com.example.basket.ui.article

import com.example.basket.entity.Article
import com.example.basket.entity.Section
import com.example.basket.entity.SortingBy
import com.example.basket.entity.UnitApp

data class ArticleScreenState(
    val article: List<List<Article>> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitApp: List<UnitApp> = emptyList(),
    val sorting: SortingBy = SortingBy.BY_NAME,
    var refresh: Boolean = true
)