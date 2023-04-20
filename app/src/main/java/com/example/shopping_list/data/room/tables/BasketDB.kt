package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Basket

//@TypeConverters(ConverterForBasketDB::class)
@Entity(tableName = "basket")
data class BasketDB(
    @PrimaryKey(autoGenerate = true) override var idBasket: Int = 0,
    override var nameBasket: String,
    override var fillBasket: Boolean = false,
    var selected:Boolean = false
): Basket
