package com.example.shopping_list.entity

interface Article{
    var idArticle: Long
    var nameArticle: String
    var unitA: UnitA
    var section: Section
    var isSelected: Boolean
    var position: Int
}
