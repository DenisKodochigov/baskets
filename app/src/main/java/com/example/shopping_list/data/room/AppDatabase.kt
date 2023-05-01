package com.example.shopping_list.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopping_list.data.room.tables.*

@Database(entities = [
    BasketEntity::class,
    ProductEntity::class,
    ArticleEntity::class,
    GroupEntity::class,
    UnitEntity::class,], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}
