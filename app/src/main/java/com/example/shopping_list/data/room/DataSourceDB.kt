package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.BasketEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.GroupArticle
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import javax.inject.Inject
import javax.inject.Singleton

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
    private fun reBuildPositionArticle(direction: Int){
        val list = getListArticle()
        val positionEnd = list.size
        val positionStart = 1
        var position = 1
        if (list.isNotEmpty()) {
            list.forEach {
                if (position + direction < positionStart) it.position = positionEnd
                else if (position + direction > positionEnd) it.position = positionStart
                else it.position = position + direction
                position++
            }
            list.forEach { dataDao.setPositionArticle(it.idArticle, it.position) }
        }
    }

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

    fun addBasket(basket: Basket): List<Basket> {
        basket.nameBasket = (basket.nameBasket[0].uppercase()+basket.nameBasket.substring(1)).trim()
        val existBasket = getBasket(basket.nameBasket)
        if (existBasket == null ) {
            dataDao.newBasket(basket as BasketEntity)
        }
        return getListBasketCount()
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        dataDao.deleteByIdBasket(basketId)
        dataDao.deleteByIdBasketProduct(basketId)
        return getListBasketCount()
    }

    fun setPositionBasket(baskets: List<Basket>, direction: Int): List<Basket>{
//        val basketId = baskets[0].basketId
//        return if (basketId != null) {
//            reBuildPosition(basketId, direction)
//            dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
//        } else emptyList()
        return emptyList()
    }

    fun getNameBasket(basketId: Long): String {
        return dataDao.getNameBasket(basketId)
    }

    /** Product*/
    fun getCountProductInBasket(basketId: Long): Int {
        return dataDao.countProductInBasket(basketId)
    }

    fun addProduct(product: Product): List<Product> {

        if (product.basketId!! > 0L ) {
            product.article.idArticle = addArticle(product.article as ArticleEntity)
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

    fun getListProducts(basketId: Long): List<Product>{
        return if (basketId > 0) dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        else emptyList()
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        dataDao.putProductInBasket(product.idProduct, basketId)
        reBuildPosition(basketId, 0)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
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

    /** Article*/
    fun addArticle(article: ArticleEntity): Long {
        article.unitId = addUnit(article.unitA as UnitEntity)
        article.groupId = addGroup(article.group as GroupEntity)

        return if (article.idArticle == 0L) {
            if (article.nameArticle != "") {
                article.nameArticle =
                    (article.nameArticle[0].uppercase()+article.nameArticle.substring(1)).trim()
                dataDao.addArticle(article)
            }
            else 1
        } else article.idArticle
    }

    fun changeArticle(articleEntity: ArticleEntity) {
        return dataDao.changeArticle(articleEntity)
    }

    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item -> mapArticle(item) }

    fun changeGroupArticle( idGroup: Long, articlesId: List<Long>): List<Article>{
        dataDao.changeGroupArticle(idGroup, articlesId)
        return getListArticle()
    }

    fun deleteSelectedArticle(articles: List<Article>): List<Article>{
        articles.forEach {
            if (it.isSelected) {
                if (dataDao.checkArticleWithProduct(it.idArticle).isEmpty())
                    dataDao.delArticle(it.idArticle)
            }
        }
        return getListArticle()
    }

    fun setPositionArticle( direction: Int): List<Article>{
        reBuildPositionArticle( direction)
        return getListArticle()
    }

    /** Group article*/
    fun getGroups(): List<GroupArticle>{
        return dataDao.getGroups()
    }

    private fun addGroup(group: GroupEntity): Long{
        return  if (group.idGroup == 0L) {
            if (group.nameGroup != "") {
                group.nameGroup = (group.nameGroup[0].uppercase()+group.nameGroup.substring(1)).trim()
                dataDao.addGroup(group)
            }  else 1
        } else group.idGroup
    }

    /** Unit*/
    private fun addUnit(unitA: UnitEntity): Long{
        return  if (unitA.idUnit == 0L) {
            if (unitA.nameUnit != "") dataDao.addUnit(unitA)  else 1
        } else unitA.idUnit
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
}