package com.example.shopping_list.ui.article

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp

data class ArticleScreenState(
    val article: List<Article> = emptyList(),
    var sections: List<Section> = emptyList(),
    var unitA: List<UnitApp> = emptyList(),
)