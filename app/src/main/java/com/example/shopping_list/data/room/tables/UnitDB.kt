package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myunit")
data class UnitDB(
    @PrimaryKey(autoGenerate = true) var idUnit: Int = 0,
    var nameUnit:String
)
