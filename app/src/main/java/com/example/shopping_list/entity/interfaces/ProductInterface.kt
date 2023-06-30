package com.example.shopping_list.entity.interfaces

interface ProductInterface {
    val idProduct: Long
    val basketId: Long
    val article: ArticleInterface
    val value: Double
    val putInBasket: Boolean
    val isSelected: Boolean
    var position: Int
}