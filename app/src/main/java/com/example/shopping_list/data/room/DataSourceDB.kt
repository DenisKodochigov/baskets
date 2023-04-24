package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    fun getListBasket(): List<Basket> = dataDao.getListBasket()

    fun getListProducts(basketId: Int): List<Product>{

        return if (basketId > -1) dataDao.getListProduct().map { item ->
            ProductEntity(
                idProduct = item.product.idProduct,
                value = item.product.value,
                basketId = item.product.basketId,
                selected = item.product.selected,
                articleId = item.article.article.idArticle,
                article = ArticleEntity(
                    idArticle = item.article.article.idArticle,
                    nameArticle = item.article.article.nameArticle,
                    group = item.article.group,
                    unitA = item.article.unitA
                )
            )
        } else emptyList()
    }

    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item ->
            ArticleEntity(
                idArticle = item.article.idArticle,
                nameArticle = item.article.nameArticle,
                group = item.group,
                unitA = item.unitA
            )
        }

    fun getGroups(): List<GroupArticle>{
        return dataDao.getGroups()
    }

    fun getUnits(): List<UnitA>{
        return dataDao.getUnits()
    }

    fun getBasket(basketName: String): Long? {
        return dataDao.checkBasketFromName(basketName)
    }

    fun addBasket(basketName: String): List<Basket> {
//        if (getBasket(basketName) == null ) {
//            dataDao.addBasket(BasketDB(nameBasket = basketName))
//        }
        return emptyList<Basket>() //getListBasket()
    }
    fun addProduct(name: String, basketId: Long): List<Product> {
        if (getProduct(name) == null ) {
            val id_article = dataDao.addArticle(ArticleEntity(nameArticle = name))
            dataDao.addProduct(ProductEntity(articleId = id_article, basketId = basketId))
        }
//        return getListProducts(basketId)
        return emptyList()
    }
    fun getProduct(productName: String): Long? {
        return dataDao.checkProductFromName(productName)
    }
    fun addGroup(groupName: String){
        dataDao.addGroup(GroupEntity(nameGroup = groupName))
    }

//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return emptyList() //dataDao.getGroupsWithProducts()
//    }
}