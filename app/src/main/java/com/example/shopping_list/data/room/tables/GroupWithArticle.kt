package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithArticle(
    @Embedded val group: GroupDB,
    @Relation(parentColumn = "idGroup", entityColumn = "groupId")
    var listArticleDB: List<ArticleDB>
)
