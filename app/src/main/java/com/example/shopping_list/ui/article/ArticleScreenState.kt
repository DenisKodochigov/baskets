package com.example.shopping_list.ui.article

import androidx.compose.runtime.Stable
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA

@Stable
data class ArticleScreenState(
    val article: List<Article> = emptyList(),
    val group: List<GroupArticle> = emptyList(),
    val unitA: List<UnitA> = emptyList(),
    val nameBasket: String = ""
)
