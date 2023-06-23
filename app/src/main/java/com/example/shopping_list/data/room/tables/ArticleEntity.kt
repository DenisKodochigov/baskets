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
    @PrimaryKey(autoGenerate = true) override var idArticle: Long = 0,
    override var nameArticle: String = "",
    override var position: Int = 0,
    var sectionId: Long = 0,
    var unitId: Long = 0,
): Article {
    @Ignore override var section: Section = SectionEntity()
    @Ignore override var unitA: UnitA = UnitEntity()
    @Ignore override var isSelected: Boolean = false
}