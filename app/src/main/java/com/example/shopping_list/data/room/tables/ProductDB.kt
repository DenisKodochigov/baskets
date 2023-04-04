package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Product

@Entity(tableName = "products", indices = [Index( value = ["name"], unique = true)])
data class ProductDB (
    @PrimaryKey(autoGenerate = true) var idProduct: Int = 0,
//    @PrimaryKey val idProduct: Int,
    val groupId: Int,
    val unitId: Int,
    var selected:Boolean?,
    @Embedded var product: Product?
)