package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.ArticleDB
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.data.room.tables.UnitDB
import com.example.shopping_list.entity.*
import com.example.shopping_list.utils.log
import java.util.*
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
    fun getListProducts(basketId: Long): List<Product> = dataSourceDB.getListProducts(basketId)
    fun buildPositionProduct(basketId: Long) = dataSourceDB.buildPositionProduct(basketId)

    fun addProduct(product: Product, basketId: Long): List<Product> =
        dataSourceDB.addProduct(product, basketId)


    fun putProductInBasket(product: Product, basketId: Long): List<Product> {
        return dataSourceDB.putProductInBasket(product, basketId)
    }

    fun setPositionBasket( direction: Int): List<Basket> {
        return dataSourceDB.setPositionBasket(direction)
    }

    fun setPositionProductInBasket(basketId: Long, direction: Int): List<Product> {
        return dataSourceDB.setPositionProductInBasket(basketId, direction)
    }

    fun changeProductInBasket(product: Product, basketId: Long): List<Product> {
        return dataSourceDB.changeProductInBasket(product, basketId)
    }

    fun deleteSelectedProduct(productList: List<Product>): List<Product> {
        return dataSourceDB.deleteSelectedProduct(productList)
    }

    /** Article entity*/

    fun buildPositionArticles() = dataSourceDB.buildPositionArticle()
    fun getListArticle(): List<Article> = dataSourceDB.getListArticle()

    fun addArticle(article: Article): List<Article> {
        dataSourceDB.getAddArticle(article as ArticleDB)
        return getListArticle()
    }

    fun changeArticle(article: Article): List<Article> = dataSourceDB.changeArticle(article as ArticleDB)

    fun getSections(): List<Section> = dataSourceDB.getSections()

    fun getUnits(): List<UnitApp> = dataSourceDB.getUnits()

    fun changeSectionSelectedProduct(productList: List<Product>, idSection: Long): List<Product> =
        dataSourceDB.changeSectionSelectedProduct(productList, idSection)

    fun changeSectionSelectedArticle(articles: List<Article>, idSection: Long): List<Article> {
        val articlesId = articles.filter { it.isSelected }.map { it.idArticle }
        return dataSourceDB.changeSectionArticle(idSection, articlesId)
    }

    fun deleteSelectedArticle(articles: List<Article>): List<Article> {
        return dataSourceDB.deleteSelectedArticle(articles)
    }

    fun movePositionArticle(direction: Int): List<Article> {
        return dataSourceDB.movePositionArticle(direction)
    }

    fun deleteUnits(deleteUnits: List<UnitApp>): List<UnitApp> {
        dataSourceDB.deleteUnits(deleteUnits)
        return getUnits()
    }
    fun changeUnit(unit: UnitApp): List<UnitApp> {
        dataSourceDB.getAddUnit(unit as UnitDB)
        return getUnits()
    }

//    fun sectionsFlow(): Flow<List<Section>> = dataSourceDB.sectionFlow()
//    fun unitsFlow(): Flow<List<UnitA>> = dataSourceDB.unitsFlow()
}