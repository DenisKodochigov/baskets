package com.example.shopping_list.data.room.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shopping_list.entity.GroupArticle

@Entity(tableName = "tb_group", indices = [Index( value = ["nameGroup"], unique = true)])
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) override var idGroup: Long = 0,
    override var nameGroup:String = ""
): GroupArticle