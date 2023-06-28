package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Section

@Entity(tableName = "tb_section", indices = [Index( value = ["nameSection"], unique = true)])
data class SectionEntity(
    @PrimaryKey(autoGenerate = true) override var idSection: Long = 0,
    override var nameSection:String = "",
    override var colorSection: String = ""
): Section