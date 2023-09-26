package com.example.basket.ui.products

import com.example.basket.entity.Article
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

data class ProductsScreenState(
    val products: List<List<Product>> = emptyList(),
    val articles: List<Article> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitApp: List<UnitApp> = emptyList(),
    val nameBasket: String = "",
    var refresh: Boolean = true
)
