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

    fun addProduct(product: Product): List<Product> {

        if (product.basketId!! > 0L && product.article.idArticle > 0L) {
            val result = dataDao.checkProductInBasket(product.basketId!!, product.article.idArticle)
            if (dataDao.checkProductInBasket(product.basketId!!, product.article.idArticle) == null) {
                dataDao.addProduct(
                    ProductEntity(
                        value = product.value,
                        basketId = product.basketId,
                        articleId = product.article.idArticle,

                    )
                )
            }
        }
        return getListProducts(product.basketId!!)
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

    fun newArticleS(name: String): List<Article> {
        dataDao.addArticle(ArticleEntity(nameArticle = name, groupId = 1, unitId = 1))
        return getListArticle()
    }
    fun addArticle(articleEntity: ArticleEntity): Long {
        return dataDao.addArticle(articleEntity)
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

    fun addGroup(group: GroupEntity): Long{
        return dataDao.addGroup(group)
    }

    fun addUnit(unitA: UnitEntity): Long{
        return dataDao.addUnit(unitA)
    }
    fun getUnits(): List<UnitA>{
        return dataDao.getUnits()
    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return emptyList() //dataDao.getGroupsWithProducts()
//    }
}