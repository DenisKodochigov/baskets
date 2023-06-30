package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.interfaces.UnitInterface

@Entity(tableName = "tb_unit", indices = [Index( value = ["nameUnit"], unique = true)])
data class UnitTable(
    @PrimaryKey(autoGenerate = true) override var idUnit: Long = 0,
    override val nameUnit: String =""
): UnitInterface {
    @Ignore override var isSelected: Boolean = false
}