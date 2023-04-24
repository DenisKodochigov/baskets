package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Basket

//@TypeConverters(ConverterForBasketDB::class)
@Entity(tableName = "tb_basket", indices = [Index( value = ["nameBasket"], unique = true)])
data class BasketEntity(
    @PrimaryKey(autoGenerate = true) override var idBasket: Long = 0,
    override var nameBasket: String,
    override var fillBasket: Boolean = false,
    var selected:Boolean = false,
): Basket
