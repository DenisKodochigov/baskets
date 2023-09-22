package com.example.basket.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB

@Database(entities = [
    BasketDB::class,
    ProductDB::class,
    ArticleDB::class,
    SectionDB::class,
    UnitDB::class,], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
