package com.example.shopping_list.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopping_list.data.room.tables.CollectionDB
import com.example.shopping_list.data.room.tables.CrossFC
import com.example.shopping_list.data.room.tables.FilmDB

@Database(entities = [
    FilmDB::class,
    CollectionDB::class,
    CrossFC::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
