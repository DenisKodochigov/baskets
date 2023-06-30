package com.example.shopping_list.entity

import com.example.shopping_list.entity.interfaces.ArticleInterface

data class Article(
    override val idArticle: Long = 0,
    override val nameArticle: String = "",
    override val unitA: UnitApp = UnitApp(),
    override val section: Section = Section(),
    override var isSelected: Boolean = false,
    override var position: Int = 0
) : ArticleInterface {
    fun mapping(item: ArticleInterface): Article{
        return Article(
            idArticle = item.idArticle,
            nameArticle = item.nameArticle,
            unitA = item.unitA as UnitApp,
            section = item.section as Section,
            isSelected = item.isSelected,
            position = item.position,
        )
    }
}

//idArticle = ,
//nameArticle = ,
//unitA = ,
//section = ,
//isSelected = ,
//position = ,