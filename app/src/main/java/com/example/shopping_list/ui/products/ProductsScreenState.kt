package com.example.shopping_list.ui.products

import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA

data class ProductsScreenState(
    val products: List<Product> = emptyList(),
    val articles: List<Article> = emptyList(),
    val sections: List<Section> = emptyList(),
    val unitA: List<UnitA> = emptyList(),
    val nameBasket: String = ""
)
