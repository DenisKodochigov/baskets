package com.example.shopping_list.data

import android.util.Log
import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.BasketEntity
import com.example.shopping_list.entity.*
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
        return dataSourceDB.addBasket(BasketEntity(nameBasket = basketName, dateB = currentTime))
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

    fun addProduct(product: Product): List<Product> {
//        Log.d("KDS", " ${product}")
        return dataSourceDB.addProduct(product)
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product> {
        return dataSourceDB.putProductInBasket(product, basketId)
    }

    fun setPositionBasket(baskets: List<Basket>, direction: Int): List<Basket> {
        return dataSourceDB.setPositionBasket(baskets, direction)
    }

    fun setPositionProductInBasket(products: List<Product>, direction: Int): List<Product> {
        return dataSourceDB.setPositionProductInBasket(products, direction)
    }

    fun changeProductInBasket(product: Product, basketId: Long): List<Product> {
        return dataSourceDB.changeProductInBasket(product, basketId)
    }

    fun deleteSelectedProduct(productList: List<Product>): List<Product> {
        return dataSourceDB.deleteSelectedProduct(productList)
    }

    /** Article entity*/
    fun getListArticle(): List<Article> = dataSourceDB.getListArticle()

    fun addArticle(article: Article): List<Article> {
        dataSourceDB.getAddArticle(article as ArticleEntity)
        return getListArticle()
    }

    fun changeArticle(article: Article): List<Article> = dataSourceDB.changeArticle(article as ArticleEntity)

    fun getGroups(): List<GroupArticle> = dataSourceDB.getGroups()

    fun getUnits(): List<UnitA> = dataSourceDB.getUnits()

    fun changeGroupSelectedProduct(productList: List<Product>, idGroup: Long): List<Product> {
        val articles = mutableListOf<Article>()
        productList.forEach {
            if (it.isSelected) {
                it.article.isSelected = it.isSelected
                articles.add(it.article)
            }
        }
        changeGroupSelectedArticle(articles, idGroup)
        return if (productList[0].basketId > 0) getListProducts(productList[0].basketId)
        else emptyList()
    }

    fun changeGroupSelectedArticle(articles: List<Article>, idGroup: Long): List<Article> {
        val articlesId = articles.filter { it.isSelected }.map { it.idArticle }
        val listArticle = dataSourceDB.changeGroupArticle(idGroup, articlesId)
        return listArticle
    }

    fun deleteSelectedArticle(articles: List<Article>): List<Article> {
        return dataSourceDB.deleteSelectedArticle(articles)
    }

    fun setPositionArticle(direction: Int): List<Article> {
        return dataSourceDB.setPositionArticle(direction)
    }
//    fun getBasketProducts(basket:BasketDB): List<ProductDB>{
//        var listProduct = emptyList<ProductDB>()
//        dataSourceDB.getBasketProducts(basket).forEach { item->
//            listProduct = item.listProductDB
//        }
//        return listProduct
//    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return dataSourceDB.getGroupsWithProduct()
//    }
}