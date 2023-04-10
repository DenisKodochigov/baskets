package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
//@TypeConverters(ConverterForBasketDB::class)
@Entity(tableName = "basket")
data class BasketDB(
    @PrimaryKey(autoGenerate = true) var idBasket: Int = 0,
    var basketName: String?,
    var selected:Boolean?
)
