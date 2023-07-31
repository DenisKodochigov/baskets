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
                        database.dataDao().newBasket(BasketDB(nameBasket = "Test"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "1"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "2"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "3"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "4"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "5"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "6"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "7"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "8"))
                        database.dataDao().newBasket(BasketDB(nameBasket = "9"))
                        database.dataDao().addSection(SectionDB(nameSection = appContext.getString(R.string.name_section)))
                        database.dataDao().addSection(SectionDB(nameSection = "Продукты"))
                        database.dataDao().addSection(SectionDB(nameSection = "Молочные"))
                        database.dataDao().addSection(SectionDB(nameSection = "Химия"))
                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_st)))
                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_kg)))
                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_gr)))
                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_l)))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "Молоко-2", sectionId = 2, unitId = 4))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "Масло-2", sectionId = 2, unitId = 3))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "Клей-3", sectionId = 3, unitId = 1))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "Картошка-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "s-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "d-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "f-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "g-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "h-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "j-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "k-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "l-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "z-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "x-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "c-2", sectionId = 2, unitId = 2))
                        database.dataDao().addProduct(ProductDB(articleId = 1, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 2, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 3, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 4, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 5, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 6, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 7, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 8, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 9, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductDB(articleId = 10, value = 1.0, basketId = 1))
                    }
                }
            })
            .build()
//        database = Room.databaseBuilder(appContext, AppDatabase::class.java, "data.db")
//            .addCallback( object: RoomDatabase.Callback(){
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    ioThread {
////                        database.dataDao().newBasket(BasketEntity(nameBasket = "Test"))
//                        database.dataDao().addSection(SectionDB(nameSection = appContext.getString(R.string.name_section)))
//                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_st)))
//                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_kg)))
//                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_gr)))
//                        database.dataDao().addUnit(UnitDB(nameUnit = appContext.getString(R.string.unit_l)))
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

