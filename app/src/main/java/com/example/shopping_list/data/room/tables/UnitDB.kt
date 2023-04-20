package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Unit

@Entity(tableName = "myunit")
data class UnitDB(
    @PrimaryKey(autoGenerate = true) var idUnit: Int = 0,
    override var nameUnit:String
): Unit
