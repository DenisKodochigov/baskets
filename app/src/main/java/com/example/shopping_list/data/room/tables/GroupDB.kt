package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mygroup")
data class GroupDB(
    @PrimaryKey(autoGenerate = true) var idGroup: Int = 0,
    val nameGroup:String
)