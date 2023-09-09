package com.example.basket.data.room.tables

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.basket.entity.Basket

@Entity(tableName = "tb_basket", indices = [Index( value = ["nameBasket"], unique = true)])
data class BasketDB(
    @PrimaryKey(autoGenerate = true) override var idBasket: Long = 0,
    override var dateB: Long = 0,
    override var nameBasket: String = "",
    override var fillBasket: Boolean = false,
    override var quantity: Int = 0,
    override var position: Int = 0,
): Basket {
    @Ignore override var isSelected: Boolean = false
}

