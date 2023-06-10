package com.example.shopping_list.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shopping_list.R
import com.example.shopping_list.data.room.AppDatabase
import com.example.shopping_list.data.room.DataDao
import com.example.shopping_list.data.room.tables.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    lateinit var database: AppDatabase
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {

        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .addCallback( object: RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        database.dataDao().newBasket(BasketEntity(nameBasket = "Test"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "1"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "2"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "3"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "4"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "5"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "6"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "7"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "8"))
                        database.dataDao().newBasket(BasketEntity(nameBasket = "9"))
                        database.dataDao().addGroup(GroupEntity(nameGroup = appContext.getString(R.string.name_group)))
                        database.dataDao().addGroup(GroupEntity(nameGroup = "Продукты"))
                        database.dataDao().addUnit(UnitEntity(nameUnit = "шт"))
                        database.dataDao().addUnit(UnitEntity(nameUnit = "кг"))
                        database.dataDao().addUnit(UnitEntity(nameUnit = "гр"))
                        database.dataDao().addUnit(UnitEntity(nameUnit = "л"))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "Молоко", groupId = 1, unitId = 4))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "Масло", groupId = 1, unitId = 3))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "Клей", groupId = 1, unitId = 1))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "Картошка", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "s", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "d", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "f", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "g", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "h", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "j", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "k", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "l", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "z", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "x", groupId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleEntity(nameArticle = "c", groupId = 1, unitId = 2))
                        database.dataDao().addProduct(ProductEntity(articleId = 1, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 2, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 3, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 4, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 5, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 6, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 7, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 8, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 9, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductEntity(articleId = 10, value = 1.0, basketId = 1))

                    }
                }
            })
            .build()
//        database = Room.databaseBuilder(appContext, AppDatabase::class.java, "data.db")
//            .addCallback( object: RoomDatabase.Callback(){
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    ioThread {
//                        database.dataDao().newBasket(BasketEntity(nameBasket = "Test"))
//                        database.dataDao().addGroup(GroupEntity(nameGroup = appContext.getString(R.string.name_group)))
//                        database.dataDao().addUnit(UnitEntity(nameUnit = appContext.getString(R.string.name_unit1)))
//                        database.dataDao().addUnit(UnitEntity(nameUnit = appContext.getString(R.string.unit_gr)))
//                        database.dataDao().addUnit(UnitEntity(nameUnit = appContext.getString(R.string.unit_kg)))
//                        database.dataDao().addUnit(UnitEntity(nameUnit = appContext.getString(R.string.unit_l)))
//                    }
//                }
//            })
//            .build()
//        database = Room.databaseBuilder( appContext, AppDatabase::class.java, "data.db").build()
        return database
    }

    @Provides
    fun provideDataDao(database: AppDatabase): DataDao {
        return database.dataDao()
    }
}

