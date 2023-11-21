package com.example.basket.domain.logicBottomSheet

import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Article
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp

fun choiceSection(section: Section?, enterNameSection: String, section0: Section): SectionDB {
    return if (section != null && enterNameSection.isNotEmpty()) {
                if (section.nameSection == enterNameSection){
                    section as SectionDB
                } else {
                    SectionDB(nameSection = enterNameSection)
                }
            } else if (section != null && enterNameSection.isEmpty()) {
                section as SectionDB
            } else if (section == null && enterNameSection.isNotEmpty()) {
                SectionDB(nameSection = enterNameSection)
            } else {
                section0 as SectionDB
            }
}

fun choiceUnit(unt: UnitApp?, enterNameUnit: String, unit0: UnitApp): UnitDB {
    return if (unt != null && enterNameUnit.isNotEmpty()) {
                if (unt.nameUnit == enterNameUnit){
                    unt as UnitDB
                } else {
                    UnitDB(nameUnit = enterNameUnit)
                }
            } else if (unt != null && enterNameUnit.isEmpty()) {
                unt as UnitDB
            } else if (unt == null && enterNameUnit.isNotEmpty()) {
                UnitDB(nameUnit = enterNameUnit)
            } else {
                unit0 as UnitDB
            }
}
fun choiceArticle(article: Article?, enterNAmeArticle: String, section: SectionDB, unt: UnitDB): ArticleDB{
    return if (article != null){
                if (article.nameArticle != enterNAmeArticle){
                    ArticleDB(
                        nameArticle = enterNAmeArticle,
                        section = section,
                        unitApp = unt
                    )
                } else {
                    article as ArticleDB
                }
            } else {
                ArticleDB(
                    nameArticle = enterNAmeArticle,
                    section = section,
                    unitApp = unt
                )
            }
}