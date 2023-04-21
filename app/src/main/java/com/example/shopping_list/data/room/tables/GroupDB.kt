package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.GroupArticle

@Entity(tableName = "mygroup")
data class GroupDB(
    @PrimaryKey(autoGenerate = true) var idGroup: Int = 0,
    override var nameGroup:String
): GroupArticle