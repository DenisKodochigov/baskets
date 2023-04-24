package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.UnitA

@Entity(tableName = "tb_unit", indices = [Index( value = ["nameUnit"], unique = true)])
data class UnitEntity(
    @PrimaryKey(autoGenerate = true) override var idUnit: Long = 0,
    override var nameUnit: String =""
): UnitA
