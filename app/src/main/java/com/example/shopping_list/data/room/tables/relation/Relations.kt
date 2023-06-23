package com.example.shopping_list.data.room.tables.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity

data class ArticleObj(
    @Embedded val article: ArticleEntity,
    @Relation(
        parentColumn = "unitId",
        entityColumn = "idUnit"
    ) val unitA: UnitEntity,
    @Relation(
        parentColumn = "sectionId",
        entityColumn = "idSection"
    ) val section: SectionEntity,
)
data class ProductObj (
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "idArticle",
        entity = ArticleEntity::class
    ) val article: ArticleObj
)
