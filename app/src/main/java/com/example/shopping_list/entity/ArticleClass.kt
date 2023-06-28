package com.example.shopping_list.entity

data class ArticleClass(
    override val idArticle: Long,
    override val nameArticle: String,
    override val unitA: UnitA,
    override val section: Section,
    override var isSelected: Boolean,
    override var position: Int
) : Article {
}

//idArticle = ,
//nameArticle = ,
//unitA = ,
//section = ,
//isSelected = ,
//position = ,