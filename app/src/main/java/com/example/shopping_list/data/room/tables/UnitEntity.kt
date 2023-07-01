package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.UnitA

@Entity(tableName = "tb_unit", indices = [Index( value = ["nameUnit"], unique = true)])
data class UnitEntity(
    @PrimaryKey(autoGenerate = true) override var idUnit: Long,
    override var nameUnit: String,
    @Ignore override var isSelected: Boolean
): UnitA {
    constructor(nameUnit: String) :this(0,"",false)
}
