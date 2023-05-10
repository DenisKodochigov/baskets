package com.example.shopping_list.data.room

import android.util.Log
import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.BasketCountObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import com.example.shopping_list.entity.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    /** Manage position product*/

    private fun reBuildPosition(basketId: Long, direction: Int){
        val listProduct = getListProducts(basketId)
        val positionEnd = listProduct.size
        var position = 1
        if (listProduct.isNotEmpty()) {
            listProduct.forEach { if (it.putInBasket) it.position = position++ }
            val positionStart = position
            listProduct.forEach {
                if (!it.putInBasket) {
                    if (position + direction < positionStart) it.position = positionEnd
                    else if (position + direction > positionEnd) it.position = positionStart
                    else it.position = position + direction
                    position++
                }
            }
            listProduct.forEach {
                if (it.basketId != null) {
                    dataDao.setPositionProductInBasket(it.idProduct, basketId, it.position)
                } }
        }
    }

    private fun getListBasket(): List<Basket> = dataDao.getListBasket()

    fun changeNameBasket(basket: Basket): List<Basket>{
        dataDao.changeNameBasket(basket.idBasket, basket.nameBasket)
        return getListBasketCount()
    }

    fun getListBasketCount(): List<Basket> {
        val result = dataDao.getListBasket()
        result.forEach {
            it.quantity = dataDao.countProductInBasket(it.idBasket)
        }
        return result
    }

    private fun getBasket(basketName: String): Long? {
        return dataDao.checkBasketFromName(basketName)
    }

    fun newBasket(basket: Basket): List<Basket> {
        if (getBasket(basket.nameBasket) == null ) {
            dataDao.newBasket(basket as BasketEntity)
        }
        return getListBasketCount()
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        dataDao.deleteByIdBasket(basketId)
        dataDao.deleteByIdBasketProduct(basketId)
        return getListBasketCount()
    }

    fun getCountProductInBasket(basketId: Long): Int {
        return dataDao.countProductInBasket(basketId)
    }

    fun addProduct(product: Product): List<Product> {
        if (product.basketId!! > 0L && product.article.idArticle > 0L) {
            if (dataDao.checkProductInBasket(product.basketId!!, product.idProduct) == null) {
                dataDao.addProduct(
                    ProductEntity(
                        value = product.value,
                        basketId = product.basketId,
                        articleId = product.article.idArticle,
                        position = product.position
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
        return if (basketId > 0) dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        else emptyList()
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        dataDao.putProductInBasket(product.idProduct, basketId)
        reBuildPosition(basketId, 0)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun setPositionBasket(baskets: List<Basket>, direction: Int): List<Basket>{
//        val basketId = baskets[0].basketId
//        return if (basketId != null) {
//            reBuildPosition(basketId, direction)
//            dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
//        } else emptyList()
        return emptyList()
    }

    fun setPositionProductInBasket(products: List<Product>, direction: Int): List<Product>{
        val basketId = products[0].basketId
        return if (basketId != null) {
            reBuildPosition(basketId, direction)
            dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        } else emptyList()
    }

    fun deleteSelectedProduct(products: MutableList<Product>): List<Product>{
        val basketId = products[0].basketId
        return if (basketId != null) {
            products.forEach { product->
                if (product.isSelected) dataDao.deleteSelectedProduct(product.idProduct, basketId)
            }
            reBuildPosition(basketId, 0)
            dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        }
        else emptyList()
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
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun newArticleS(name: String): List<Article> {
        dataDao.addArticle(ArticleEntity(nameArticle = name, groupId = 1, unitId = 1))
        return getListArticle()
    }

    fun addArticle(articleEntity: ArticleEntity): Long {
        return dataDao.addArticle(articleEntity)
    }

    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item -> mapArticle(item) }

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
    private fun mapProduct(entity: ProductObj):ProductEntity{
        return ProductEntity(
            idProduct = entity.product.idProduct,
            value = entity.product.value,
            position = entity.product.position,
            basketId = entity.product.basketId,
            putInBasket = entity.product.putInBasket,
            articleId = entity.article.article.idArticle,
            article = ArticleEntity(
                idArticle = entity.article.article.idArticle,
                nameArticle = entity.article.article.nameArticle,
                group = entity.article.group,
                unitA = entity.article.unitA
            )
        )
    }

    private fun mapArticle(obj: ArticleObj): ArticleEntity{
        return ArticleEntity(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle,
            group = obj.group,
            unitA = obj.unitA
        )
    }

    private fun mapBasket(obj: BasketCountObj): Basket{
        return obj.basket.also { it.quantity = obj.count }
    }
}