package com.example.basket.data

import com.example.basket.data.room.DataSourceDB
import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.entity.Article
import com.example.basket.entity.Basket
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.SortingBy
import com.example.basket.entity.UnitApp
import com.example.basket.utils.createDoubleLisArticle
import com.example.basket.utils.createDoubleListProduct
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    /** Basket entity*/
    fun getListBasket(): List<Basket> {
        return dataSourceDB.getListBasketCount()
    }

    fun addBasket(basketName: String): List<Basket> {
        val currentTime = Date().time
        return dataSourceDB.addBasket(BasketDB(nameBasket = basketName, dateB = currentTime))
    }

    fun changeNameBasket(basket: Basket): List<Basket> {
        return dataSourceDB.changeNameBasket(basket)
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        return dataSourceDB.deleteBasket(basketId)
    }

    fun getNameBasket(basketId: Long): String {
        return dataSourceDB.getNameBasket(basketId)
    }

    /** Product entity*/
    fun getListProducts(basketId: Long): List<List<Product>> =
        createDoubleListProduct( dataSourceDB.getListProducts(basketId) )

    fun addProduct(product: Product, basketId: Long): List<List<Product>> =
        createDoubleListProduct( dataSourceDB.addProduct(product, basketId))


    fun putProductInBasket(product: Product, basketId: Long): List<List<Product>> {
        return createDoubleListProduct( dataSourceDB.putProductInBasket(product, basketId) )
    }

    fun changeProductInBasket(product: Product, basketId: Long): List<List<Product>> {
        return createDoubleListProduct( dataSourceDB.changeProductInBasket(product, basketId) )
    }

    fun deleteSelectedProduct(productList: List<Product>): List<List<Product>> {
        return createDoubleListProduct(dataSourceDB.deleteSelectedProduct(productList))
    }

    /** Article entity*/

    fun getListArticle(sortingBy: SortingBy ): List<List<Article>> =
        createDoubleLisArticle( dataSourceDB.getListArticle(), sortingBy)
    fun getListArticle(): List<Article> = dataSourceDB.getListArticle()

    fun addArticle(article: Article, sortingBy: SortingBy ): List<List<Article>> {
        dataSourceDB.getAddArticle(article as ArticleDB)
        return getListArticle( sortingBy )
    }

    fun changeArticle(article: Article, sortingBy: SortingBy): List<List<Article>> =
        createDoubleLisArticle(dataSourceDB.changeArticle(article as ArticleDB), sortingBy)

    fun getSections(): List<Section> = dataSourceDB.getSections()

    fun getUnits(): List<UnitApp> = dataSourceDB.getUnits()

    fun changeSectionSelectedProduct(productList: List<Product>, idSection: Long): List<List<Product>> =
        createDoubleListProduct( dataSourceDB.changeSectionSelectedProduct(productList, idSection) )

    fun changeSectionSelectedArticle(
        articles: List<Article>,
        idSection: Long,
        sortingBy: SortingBy): List<List<Article>>
    {
        val articlesId = articles.filter { it.isSelected }.map { it.idArticle }
        return createDoubleLisArticle( dataSourceDB.changeSectionArticle(idSection, articlesId), sortingBy)
    }

    fun deleteSelectedArticle(articles: List<Article>, sortingBy: SortingBy): List<List<Article>> {
        return createDoubleLisArticle( dataSourceDB.deleteSelectedArticle(articles), sortingBy)
    }

    fun deleteUnits(deleteUnits: List<UnitApp>): List<UnitApp> {
        dataSourceDB.deleteUnits(deleteUnits)
        return getUnits()
    }
    fun changeUnit(unit: UnitApp): List<UnitApp> {
        dataSourceDB.getAddUnit(unit as UnitDB)
        return getUnits()
    }
    fun changeSection(section: Section): List<Section> =
        dataSourceDB.changeSection(section)
    fun deleteSections(sections: List<Section>): List<Section> {
        dataSourceDB.deleteSections(sections)
        return getSections()
    }

//    fun sectionsFlow(): Flow<List<Section>> = dataSourceDB.sectionFlow()
//    fun unitsFlow(): Flow<List<UnitA>> = dataSourceDB.unitsFlow()
}