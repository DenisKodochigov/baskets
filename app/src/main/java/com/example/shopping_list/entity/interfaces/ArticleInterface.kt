package com.example.shopping_list.entity.interfaces

interface ArticleInterface{
    val idArticle: Long
    val nameArticle: String
    val unitA: UnitInterface
    val section: SectionInterface
    var isSelected: Boolean
    var position: Int
}
