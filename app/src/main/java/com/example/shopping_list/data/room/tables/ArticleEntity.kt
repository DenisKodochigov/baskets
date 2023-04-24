package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.UnitA

@Entity(tableName = "tb_article", indices = [Index( value = ["nameArticle"], unique = true)])
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) override var idArticle: Long = 0,
    override var nameArticle: String = "",
    var groupId: Long = -1,
    var unitId: Long = -1,
    @Ignore override var group: GroupArticle = GroupEntity(),
    @Ignore override var unitA: UnitA = UnitEntity()
): Article
