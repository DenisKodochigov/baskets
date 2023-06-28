package com.example.shopping_list.entity

interface Article{
    val idArticle: Long
    val nameArticle: String
    val unitA: UnitA
    val section: Section
    var isSelected: Boolean
    var position: Int
}
