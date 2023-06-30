package com.example.shopping_list.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopping_list.data.room.tables.*

@Database(entities = [
    BasketTable::class,
    ProductTable::class,
    ArticleTable::class,
    SectionTable::class,
    UnitTable::class,], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
