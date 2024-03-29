package com.example.basket.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.basket.entity.Article
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

@Entity(tableName = "tb_article", indices = [Index(value = ["nameArticle"], unique = true)])
data class ArticleDB(
    @PrimaryKey(autoGenerate = true) override var idArticle: Long,
    override var nameArticle: String,
    override var position: Int,
    var sectionId: Long,
    var unitId: Long,
    @Ignore override var isSelected: Boolean,
    @Ignore override var section: Section,
    @Ignore override var unitApp: UnitApp,
): Article {

    constructor(
        nameArticle: String,
        sectionId: Long,
        unitId: Long,
    ): this(
        idArticle= 0,
        nameArticle = "",
        position = 0,
        sectionId= 0,
        unitId= 0,
        isSelected = false,
        section = SectionDB(),
        unitApp = UnitDB(0, "", false)
    ){
        this.nameArticle = nameArticle
        this.sectionId = sectionId
        this.unitId = unitId
    }
    @Ignore constructor(
        idArticle: Long,
        nameArticle: String,
        position: Int,
        sectionId: Long,
        unitId: Long,
    ): this(
        idArticle= 0,
        nameArticle = "",
        position = 0,
        sectionId= 0,
        unitId= 0,
        isSelected = false,
        section = SectionDB(),
        unitApp = UnitDB(0, "", false)
    ){
        this.idArticle = idArticle
        this.nameArticle = nameArticle
        this.position = position
        this.sectionId = sectionId
        this.unitId = unitId
    }
    @Ignore constructor(
        idArticle: Long,
        nameArticle: String,
        position: Int,
        isSelected: Boolean,
        section: SectionDB,
        unitApp: UnitDB
    ): this(
        idArticle= 0,
        nameArticle = "",
        position = 0,
        sectionId= 0,
        unitId= 0,
        isSelected = false,
        section = SectionDB(),
        unitApp = UnitDB(0, "", false)
    ){
        this.idArticle = idArticle
        this.nameArticle = nameArticle
        this.position = position
        this.isSelected = isSelected
        this.section = section
        this.unitApp = unitApp
    }
    @Ignore constructor(
        nameArticle: String,
        section: SectionDB,
        unitApp: UnitDB
    ): this(
        idArticle= 0,
        nameArticle = "",
        position = 0,
        sectionId= 0,
        unitId= 0,
        isSelected = false,
        section = SectionDB(),
        unitApp = UnitDB(0, "", false)
    ){
        this.nameArticle = nameArticle
        this.section = section
        this.unitApp = unitApp
    }
}