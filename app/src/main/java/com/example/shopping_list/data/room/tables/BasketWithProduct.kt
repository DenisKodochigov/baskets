package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.shopping_list.entity.Product

data class BasketWithProduct(
    @Embedded val basket: BasketDB,
    @Relation(
        parentColumn = "idBasket",
        entityColumn = "idProduct",
        associateBy = Junction(BasketToProductDB::class)
    )
    val listProductDB: List<ProductDB>
)
