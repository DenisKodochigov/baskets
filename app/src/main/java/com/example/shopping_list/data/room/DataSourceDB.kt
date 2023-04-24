package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    fun getListBasket(): List<Basket> = dataDao.getListBasket()

    fun getBasket(basketName: String): Long? {
        return dataDao.checkBasketFromName(basketName)
    }

    fun newBasket(basketName: String): List<Basket> {
        if (getBasket(basketName) == null ) {
            dataDao.newBasket(BasketEntity(nameBasket = basketName))
        }
        return getListBasket()
    }

    fun newProduct(product: Product): List<Product> {
        if (dataDao.checkProductInBasket(product.basketId, product.idProduct) == null) {
            dataDao.newProduct(ProductEntity(
                value = product.value,
                basketId = product.basketId,
                articleId = product.article.idArticle,))
        }
        return getListProducts(product.basketId)
    }

    fun getProduct(productName: String): Long? {
        return dataDao.checkProductFromName(productName)
    }

    fun getListProducts(basketId: Long): List<Product>{

        return if (basketId > 0) dataDao.getListProduct(basketId).map { item ->
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
        } else dataDao.getListProductAll().map { item ->
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
        }
    }

    fun newArticle(name: String): List<Article> {
        dataDao.newArticle(ArticleEntity(nameArticle = name, groupId = 1, unitId = 1))
        return getListArticle()
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

    fun addGroup(groupName: String){
        dataDao.addGroup(GroupEntity(nameGroup = groupName))
    }

    fun getUnits(): List<UnitA>{
        return dataDao.getUnits()
    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return emptyList() //dataDao.getGroupsWithProducts()
//    }
}