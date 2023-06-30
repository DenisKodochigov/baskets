package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.interfaces.SectionInterface

@Entity(tableName = "tb_section", indices = [Index( value = ["nameSection"], unique = true)])
data class SectionTable(
    @PrimaryKey(autoGenerate = true) override var idSection: Long = 0,
    override var nameSection:String = "",
    override var colorSection: String = ""
): SectionInterface