package com.example.basket.entity

interface Article{
    val idArticle: Long
    val nameArticle: String
    val unitApp: UnitApp
    val section: Section
    var isSelected: Boolean
    var position: Int
}
