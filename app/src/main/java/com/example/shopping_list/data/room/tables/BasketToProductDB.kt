package com.example.shopping_list.data.room.tables

import androidx.room.Entity

@Entity(tableName = "basketproduct", primaryKeys = ["idBasket", "idProduct"])
data class BasketToProductDB(
    val idBasket: Int,
    val idProduct: Int
)
