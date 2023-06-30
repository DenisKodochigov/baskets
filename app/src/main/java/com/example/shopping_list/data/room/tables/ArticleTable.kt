package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.interfaces.ArticleInterface
import com.example.shopping_list.entity.interfaces.SectionInterface
import com.example.shopping_list.entity.interfaces.UnitInterface

@Entity(tableName = "tb_article", indices = [Index( value = ["nameArticle"], unique = true)])
data class ArticleTable(
    @PrimaryKey(autoGenerate = true) override val idArticle: Long = 0,
    override val nameArticle: String = "",
    override var position: Int = 0,
    val sectionId: Long = 0,
    val unitId: Long = 0,
): ArticleInterface {
    @Ignore override val section: SectionInterface = SectionTable()
    @Ignore override val unitA: UnitInterface = UnitTable()
    @Ignore override var isSelected: Boolean = false
}