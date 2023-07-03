package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.UnitApp

@Entity(tableName = "tb_unit", indices = [Index( value = ["nameUnit"], unique = true)])
data class UnitDB(
    @PrimaryKey(autoGenerate = true) override var idUnit: Long,
    override var nameUnit: String,
    @Ignore override var isSelected: Boolean
): UnitApp {
    constructor(
        nameUnit: String
    ) :this(
        idUnit = 0,
        nameUnit = "",
        isSelected = false)
    {
        this.nameUnit = nameUnit
    }
    @Ignore constructor(
        idUnit: Long,
        nameUnit: String
    ) :this(
        idUnit = 0,
        nameUnit = "",
        isSelected = false)
    {
        this.nameUnit = nameUnit
        this.idUnit = idUnit
    }
    @Ignore constructor(): this(0,"",false)
}
