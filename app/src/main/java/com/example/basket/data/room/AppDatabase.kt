package com.example.basket.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.basket.data.room.tables.*

@Database(entities = [
    BasketDB::class,
    ProductDB::class,
    ArticleDB::class,
    SectionDB::class,
    UnitDB::class,], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
