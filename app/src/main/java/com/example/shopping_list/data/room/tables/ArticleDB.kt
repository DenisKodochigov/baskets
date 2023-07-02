package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp

@Entity(tableName = "tb_article", indices = [Index(value = ["nameArticle"], unique = true)])
data class ArticleDB(
    @PrimaryKey(autoGenerate = true) override val idArticle: Long,
    override val nameArticle: String,
    override var position: Int,
    val sectionId: Long,
    val unitId: Long,
    @Ignore override val section: Section,
    @Ignore override val unitApp: UnitApp,
    @Ignore override var isSelected: Boolean,
) : Article {

    constructor(idArticle: Long, nameArticle: String, position: Int, sectionId: Long, unitId: Long)
            : this(0, "", 0, 0, 0, SectionDB(), UnitDB(0, "", false), false)

    @Ignore
    constructor(idArticle: Long, nameArticle: String, position: Int, unitApp: UnitApp, section: Section,
                isSelected: Boolean
    ) : this(0, "", 0, 0, 0, SectionDB() as Section, UnitDB(0, "", false) as UnitApp, false)

    @Ignore
    constructor( idArticle: Long, unitId: Long, sectionId: Long,  nameArticle: String,) :
            this(0, "", 0, 0, 0, SectionDB() as Section, UnitDB(0, "", false) as UnitApp, false)
    @Ignore
    constructor( nameArticle: String, unitId: Long, sectionId: Long,  ) :
            this(0, "", 0, 0, 0, SectionDB() as Section, UnitDB(0, "", false) as UnitApp, false)
}