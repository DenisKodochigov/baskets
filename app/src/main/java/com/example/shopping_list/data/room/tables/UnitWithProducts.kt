package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Relation
import kotlin.Unit

data class UnitWithProducts(
    @Embedded val unit: UnitDB,
    @Relation(parentColumn = "idUnit", entityColumn = "unitId")
    var listProductDB: List<ProductDB>
)
