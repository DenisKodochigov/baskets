package com.example.shopping_list.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopping_list.data.room.tables.ProductDB
import com.example.shopping_list.data.room.tables.BasketToProductDB
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.data.room.tables.GroupDB
import com.example.shopping_list.data.room.tables.UnitDB

@Database(entities = [
    BasketDB::class,
    ProductDB::class,
    GroupDB::class,
    UnitDB::class,
    BasketToProductDB::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
