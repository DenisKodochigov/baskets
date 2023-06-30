package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.BasketTable
import com.example.shopping_list.data.room.tables.UnitTable
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp
import com.example.shopping_list.entity.interfaces.ArticleInterface
import com.example.shopping_list.entity.interfaces.ProductInterface
import com.example.shopping_list.entity.interfaces.UnitInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    /** Basket entity*/
    fun getListBasket(): List<Basket> {
        return dataSourceDB.getListBasketCount().map { Basket().mapping (it) }
    }

    fun addBasket(basketName: String): List<Basket> {
        val currentTime = Date().time
        return dataSourceDB.addBasket( BasketTable(nameBasket = basketName, dateB = currentTime))
            .map { Basket().mapping (it) }
    }

    fun changeNameBasket(basket: Basket): List<Basket> {
        return dataSourceDB.changeNameBasket(basket).map { Basket().mapping (it) }
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        return dataSourceDB.deleteBasket(basketId).map { Basket().mapping (it) }
    }

    fun setPositionBasket( direction: Int): List<Basket> {
        return dataSourceDB.setPositionBasket(direction).map { Basket().mapping (it) }
    }

    fun getNameBasket(basketId: Long): String = dataSourceDB.getNameBasket(basketId)


    /** Product entity*/
    private fun movePositionProduct(basketId: Long, direction: Int): List<Product>{
        val listProduct = dataSourceDB.getListProducts(basketId)
        val positionEnd = listProduct.size
        var position = 1
        if (listProduct.isNotEmpty()) {
            val positionStart = position
            listProduct.forEach {
                if (position + direction < positionStart) it.position = positionEnd
                else if (position + direction > positionEnd) it.position = positionStart
                else it.position = position + direction
                position++
            }
            listProduct.forEach {
                dataSourceDB.setPositionProductInBasket(it.idProduct, basketId, it.position)
            }
        }
        return listProduct.sortedBy { it.position }.map { Product().mapping(it) }
    }
    fun setPositionProductInBasket(basketId: Long, direction: Int): List<Product> {
        return if (basketId > 0) movePositionProduct(basketId, direction) else emptyList()
    }

    fun changeProductInBasket(product: ProductInterface, basketId: Long): List<Product> {
        return dataSourceDB.changeProductInBasket(product, basketId).map { Product().mapping(it) }
    }

    fun getListProducts(basketId: Long): List<Product> =
        dataSourceDB.getListProducts(basketId).map { Product().mapping(it) }
    fun buildPositionProduct(basketId: Long) = dataSourceDB.buildPositionProduct(basketId)

    fun addProduct(product: Product): List<Product> {
//        Log.d("KDS", " ${product}")
        return dataSourceDB.addProduct(product).map { Product().mapping(it) }
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product> {
        return dataSourceDB.putProductInBasket(product, basketId).map { Product().mapping(it) }
    }

    fun deleteSelectedProduct(productList: List<Product>): List<Product> {
        return dataSourceDB.deleteSelectedProduct(productList).map { Product().mapping(it) }
    }

    fun changeSectionSelectedProduct(productList: List<ProductInterface>, idSection: Long): List<Product> =
        dataSourceDB.changeSectionSelectedProduct(productList, idSection).map { Product().mapping(it) }

    /** Article entity*/
    fun getListArticle(): List<Article> = dataSourceDB.getListArticle().map { Article().mapping(it) }

    fun sortingArticle(){
        dataSourceDB.sortingArticle()
    }
    fun addArticle(article: Article): List<Article> {
        dataSourceDB.getAddArticle(article as ArticleInterface)
        return getListArticle()
    }

    fun changeArticle(article: Article): List<Article> = dataSourceDB.changeArticle( article )
        .map { Article().mapping(it) }

    fun getSections(): List<Section> = dataSourceDB.getSections().map { Section().mapping(it) }

    fun getUnits(): List<UnitApp> = dataSourceDB.getUnits().map { UnitApp().mapping(it) }


    fun changeSectionSelectedArticle(articles: List<Article>, idSection: Long): List<Article> {
        val articlesId = articles.filter { it.isSelected }.map { it.idArticle }
        return dataSourceDB.changeSectionArticle(idSection, articlesId).map { Article().mapping(it) }
    }

    fun deleteSelectedArticle(articles: List<Article>): List<Article> {
        return dataSourceDB.deleteSelectedArticle(articles).map { Article().mapping(it) }
    }

    fun setPositionArticle(direction: Int): List<Article> {
        return dataSourceDB.setPositionArticle(direction).map { Article().mapping(it) }
    }

    fun deleteUnits(deleteUnits: List<UnitInterface>): List<UnitInterface> {
        dataSourceDB.deleteUnits(deleteUnits)
        return getUnits()
    }
    fun changeUnit(unit: UnitInterface): List<UnitInterface> {
        dataSourceDB.getAddUnit(unit as UnitTable)
        return getUnits()
    }

}