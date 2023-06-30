package com.example.shopping_list.entity

import com.example.shopping_list.entity.interfaces.SectionInterface

data class Section(
    override val idSection: Long = 0 ,
    override val nameSection: String = "",
    override val colorSection: String = ""
): SectionInterface{
    fun mapping(item: SectionInterface):Section{
        return Section(
            idSection = item.idSection,
            nameSection = item.nameSection,
            colorSection = item.colorSection
        )
    }
}
