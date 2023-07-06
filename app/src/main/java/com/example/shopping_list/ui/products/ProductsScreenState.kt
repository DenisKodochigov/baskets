package com.example.shopping_list.ui.products

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitApp

data class ProductsScreenState(
    val products: List<List<Product>> = emptyList(),
    val articles: List<Article> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitApp: List<UnitApp> = emptyList(),
    val nameBasket: String = ""
)
