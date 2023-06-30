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
                        database.dataDao().newBasket(BasketTable(nameBasket = "Test"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "1"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "2"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "3"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "4"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "5"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "6"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "7"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "8"))
                        database.dataDao().newBasket(BasketTable(nameBasket = "9"))
                        database.dataDao().addSection(SectionTable(nameSection = appContext.getString(R.string.name_section)))
                        database.dataDao().addSection(SectionTable(nameSection = "Продукты"))
                        database.dataDao().addSection(SectionTable(nameSection = "Молочные"))
                        database.dataDao().addSection(SectionTable(nameSection = "Химия"))
                        database.dataDao().addUnit(UnitTable(nameUnit = "шт"))
                        database.dataDao().addUnit(UnitTable(nameUnit = "кг"))
                        database.dataDao().addUnit(UnitTable(nameUnit = "гр"))
                        database.dataDao().addUnit(UnitTable(nameUnit = "л"))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "Молоко-2", sectionId = 2, unitId = 4))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "Масло-2", sectionId = 2, unitId = 3))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "Клей-3", sectionId = 3, unitId = 1))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "Картошка-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "s-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "d-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "f-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "g-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "h-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "j-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "k-2", sectionId = 2, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "l-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "z-1", sectionId = 1, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "x-3", sectionId = 3, unitId = 2))
                        database.dataDao().addArticle(ArticleTable(nameArticle = "c-2", sectionId = 2, unitId = 2))
                        database.dataDao().addProduct(ProductTable(articleId = 1, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 2, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 3, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 4, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 5, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 6, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 7, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 8, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 9, value = 1.0, basketId = 1))
                        database.dataDao().addProduct(ProductTable(articleId = 10, value = 1.0, basketId = 1))
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
//                        database.dataDao().addSection(SectionEntity(nameSection = appContext.getString(R.string.name_section)))
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

