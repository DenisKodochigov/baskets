package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Relation

data class UnitWithArticle(
    @Embedded val unit: UnitDB,
    @Relation(parentColumn = "idUnit", entityColumn = "unitId")
    var listArticle: List<ArticleDB>
)
