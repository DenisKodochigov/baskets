package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Section

@Entity(tableName = "tb_section", indices = [Index( value = ["nameSection"], unique = true)])
data class SectionDB(
    @PrimaryKey(autoGenerate = true) override var idSection: Long = 0,
    override var nameSection:String = "",
    override var colorSection: Long = 0L,
    @Ignore override var isSelected: Boolean
): Section {
    constructor(
        nameSection: String
    ) :this(
        idSection = 0,
        nameSection = "",
        colorSection = 0L,
        isSelected = false)
    {
        this.nameSection = nameSection
    }
    @Ignore constructor(
        idSection: Long,
        nameSection: String,
    ) :this(  0,
        nameSection = "",
        colorSection = 0L,
        isSelected = false)
    {
        this.idSection = idSection
        this.nameSection = nameSection
    }
    @Ignore constructor(
        idSection: Long,
        nameSection: String,
        colorSection: Long
    ) :this(  0,
        nameSection = "",
        colorSection = 0L,
        isSelected = false)
    {
        this.idSection = idSection
        this.nameSection = nameSection
        this.colorSection = colorSection
    }
    @Ignore constructor(): this(0,"",0L,false)
}