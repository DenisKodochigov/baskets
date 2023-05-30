package com.example.shopping_list.ui.products

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA

data class StateProductsScreen(
    val products: List<Product> = emptyList(),
    val articles: List<Article> = emptyList(),
    val group: List<GroupArticle> = emptyList(),
    val unitA: List<UnitA> = emptyList(),
    val nameBasket: String = ""
)
