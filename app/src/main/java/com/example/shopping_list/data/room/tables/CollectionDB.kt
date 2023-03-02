package com.example.shopping_list.data.room.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "collections", indices = [Index( value = ["name"], unique = true)])
data class CollectionDB (
    @PrimaryKey(autoGenerate = true) val idCollection: Int = 0,
//    @Embedded var collection: Collection?
)