package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.Product

@Entity(tableName = "products", indices = [Index( value = ["nameProduct"], unique = true)])
data class ProductDB (
    @PrimaryKey(autoGenerate = true) override var idProduct: Int = 0,
    override var nameProduct: String,
    override var value: Double = 0.0,
    var groupId: Int = -1,
    var unitId: Int = -1,
    var selected:Boolean = false,
): Product