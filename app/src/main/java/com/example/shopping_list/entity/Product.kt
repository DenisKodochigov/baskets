package com.example.shopping_list.entity

interface Product {
    var idProduct: Long
    var basketId: Long
    var article: Article
    var value: Double
}