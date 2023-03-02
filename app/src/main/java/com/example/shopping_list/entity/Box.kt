package com.example.shopping_list.entity

data class Box (
    val name: String? = null,
    val products: List<Product> = emptyList()
)