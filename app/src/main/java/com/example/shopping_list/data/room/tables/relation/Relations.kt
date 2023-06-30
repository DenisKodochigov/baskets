package com.example.shopping_list.data.room.tables.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shopping_list.data.room.tables.ArticleTable
import com.example.shopping_list.data.room.tables.SectionTable
import com.example.shopping_list.data.room.tables.ProductTable
import com.example.shopping_list.data.room.tables.UnitTable

data class ArticleObj(
    @Embedded val article: ArticleTable,
    @Relation(
        parentColumn = "unitId",
        entityColumn = "idUnit"
    ) val unitA: UnitTable,
    @Relation(
        parentColumn = "sectionId",
        entityColumn = "idSection"
    ) val section: SectionTable,
)
data class ProductObj (
    @Embedded val product: ProductTable,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "idArticle",
        entity = ArticleTable::class
    ) val article: ArticleObj
)
