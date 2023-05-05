package com.example.shopping_list.data.room

import android.util.Log
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    fun getListBasket(): List<Basket> = dataDao.getListBasket()

    private fun getBasket(basketName: String): Long? {
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
                putInBasket = item.product.putInBasket,
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
                putInBasket = item.product.putInBasket,
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

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        dataDao.putProductInBasket(product.idProduct, basketId)
        return dataDao.getListProductAll().map { item ->
            ProductEntity(
                idProduct = item.product.idProduct,
                value = item.product.value,
                basketId = item.product.basketId,
                putInBasket = item.product.putInBasket,
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

    fun changeProductInBasket(product: Product, basketId: Long): List<Product>{
        if ( product.article.unitA != null) {
            if (product.article.unitA!!.idUnit != 0L) {
                if (product.article.unitA!!.idUnit != dataDao.getIdUnitFromArticle(product.article.idArticle)) {
                    dataDao.setUnitInArticle(product.article.idArticle, product.article.unitA!!.idUnit)
                }
            } else {
                dataDao.setUnitInArticle(
                    product.article.idArticle,
                    dataDao.addUnit(UnitEntity(nameUnit = product.article.unitA!!.nameUnit)))
            }
        }

        if (product.value > 0) dataDao.setValueProduct(product.idProduct, basketId, product.value)

        return dataDao.getListProductAll().map { item ->
            ProductEntity(
                idProduct = item.product.idProduct,
                value = item.product.value,
                basketId = item.product.basketId,
                putInBasket = item.product.putInBasket,
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

    fun deleteSelectedProduct(productList: MutableList<Product>): List<Product>{
        for (product in productList) {
            if (product.isSelected) product.basketId?.let {
                dataDao.deleteSelectedProduct(product.idProduct, it)
            }
        }
        return if (productList[0].basketId != null) getListProducts(productList[0].basketId!!) else emptyList()
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