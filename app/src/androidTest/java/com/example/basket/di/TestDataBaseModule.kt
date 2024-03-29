package com.example.basket.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.basket.R
import com.example.basket.data.room.AppDatabase
import com.example.basket.data.room.DataDao
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestDataBaseModule {
    lateinit var database: AppDatabase
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        database.dataDao().newBasket(BasketDB(nameBasket = "0 basket", dateB = 1L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "1 basket", dateB = 2L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "2 basket", dateB = 3L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "3 basket", dateB = 4L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "4 basket", dateB = 5L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "5 basket", dateB = 6L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "6 basket", dateB = 7L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "7 basket", dateB = 8L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "8 basket", dateB = 9L))
                        database.dataDao().newBasket(BasketDB(nameBasket = "9 basket", dateB = 10L))
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
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product s-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product d-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product f-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product g-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product h-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product j-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product k-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product l-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product z-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product x-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleDB(nameArticle = "product c-2", sectionId = 2, unitId = 2))
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
        return database
    }

    @Provides
    fun provideDataDao(database: AppDatabase): DataDao {
        return database.dataDao()
    }
}