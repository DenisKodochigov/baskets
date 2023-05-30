package com.example.shopping_list.ui.article

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA

data class ArticleScreenState(
    val article: List<Article> = emptyList(),
    var group: List<GroupArticle> = emptyList(),
    var unitA: List<UnitA> = emptyList(),
)