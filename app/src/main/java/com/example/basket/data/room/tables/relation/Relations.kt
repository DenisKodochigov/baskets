package com.example.basket.data.room.tables.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB

data class ArticleObj(
    @Embedded val article: ArticleDB,
    @Relation(
        parentColumn = "unitId",
        entityColumn = "idUnit"
    ) val unitA: UnitDB,
    @Relation(
        parentColumn = "sectionId",
        entityColumn = "idSection"
    ) val section: SectionDB,
)
data class ProductObj (
    @Embedded val product: ProductDB,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "idArticle",
        entity = ArticleDB::class
    ) val article: ArticleObj
)
