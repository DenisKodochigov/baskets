package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article

@Entity(tableName = "article", indices = [Index( value = ["nameArticle"], unique = true)])
data class ArticleDB(
    @PrimaryKey(autoGenerate = true) override var idArticle: Int = 0,
    override val nameArticle: String,
    var groupId: Int = -1,
    var unitId: Int = -1,
): Article
