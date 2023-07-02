package com.example.shopping_list.ui.article

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.SortingBy
import com.example.shopping_list.entity.UnitApp

data class ArticleScreenState(
    val article: List<Article> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitApp: List<UnitApp> = emptyList(),
    val sorting: SortingBy = SortingBy.BY_NAME
)