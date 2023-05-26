package com.example.shopping_list.ui.article

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA

data class StateArticlesScreen(
    val article: List<Article> = emptyList(),
    val group: List<GroupArticle> = emptyList(),
    val unitA: List<UnitA> = emptyList(),
)