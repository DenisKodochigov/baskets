package com.example.basket.entity
interface Product {
    val idProduct: Long
    val basketId: Long
    val article: Article
    val value: Double
    val putInBasket: Boolean
    var isSelected: Boolean
    var position: Int
}