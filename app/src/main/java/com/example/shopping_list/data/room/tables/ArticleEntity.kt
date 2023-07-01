package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitA

@Entity(tableName = "tb_article", indices = [Index( value = ["nameArticle"], unique = true)])
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) override val idArticle: Long,
    override val nameArticle: String,
    override var position: Int,
    val sectionId: Long,
    val unitId: Long,
    @Ignore override val section: Section,
    @Ignore override val unitA: UnitA,
    @Ignore override var isSelected: Boolean ,
): Article {
    constructor(idArticle: Long, nameArticle: String, position: Int, sectionId: Long, unitId: Long)
    :this(0,"", 0, 0, 0, SectionEntity(), UnitEntity(0,"", false), false)
}