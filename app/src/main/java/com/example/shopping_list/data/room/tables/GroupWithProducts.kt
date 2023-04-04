package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithProducts(
    @Embedded val group: GroupDB,
    @Relation(parentColumn = "idGroup", entityColumn = "groupId")
    var listProductDB: List<ProductDB>
)
