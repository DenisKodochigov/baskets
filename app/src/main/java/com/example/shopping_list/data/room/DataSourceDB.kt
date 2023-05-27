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
class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

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
                dataDao.setPositionProductInBasket(it.idProduct, basketId, it.position)
            }
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
        result.forEach { it.quantity = dataDao.countProductInBasket(it.idBasket) }
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
    fun addProduct(product: Product): List<Product> {
        if (product.basketId > 0L ) {
            product.article.idArticle = getAddArticle(product.article as ArticleEntity)?.idArticle ?: 0
            if (product.article.idArticle != 0L) {
                if (dataDao.checkProductInBasket(product.basketId, product.idProduct) == null) {
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
        }
        return getListProducts(product.basketId)
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
        return if (basketId > 0) {
            reBuildPosition(basketId, direction)
            dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        } else emptyList()
    }
    fun changeProductInBasket(product: Product, basketId: Long): List<Product>{
        getAddArticle(product.article as ArticleEntity)
        if (product.value > 0) dataDao.setValueProduct(product.idProduct, basketId, product.value)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun deleteSelectedProduct(products: List<Product>): List<Product>{
        val basketId = products[0].basketId
        val listId = products.filter { it.isSelected }.map { it.idProduct }
        dataDao.deleteProducts(listId, basketId)
        reBuildPosition(basketId, 0)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }
    /** Article*/
    fun deleteSelectedArticle(articles: List<Article>): List<Article>{
        articles.forEach {
            if (it.isSelected) {
                if (dataDao.checkArticleWithProduct(it.idArticle).isEmpty())
                    dataDao.delArticle(it.idArticle)
            }
        }
        return getListArticle()
    }

    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item -> mapArticle(item) }

    fun setPositionArticle( direction: Int): List<Article>{
        reBuildPositionArticle( direction)
        return getListArticle()
    }

    fun getAddArticle(article: ArticleEntity): ArticleEntity? {
        article.unitId = getAddUnit(article.unitA as UnitEntity).idUnit
        article.groupId = getAddGroup(article.group as GroupEntity).idGroup
        article.nameArticle = toUpFirstChar(article.nameArticle)
        if (article.idArticle == 0L && article.nameArticle != "") {
                article.idArticle = dataDao.addArticle(article)
        }
        return if (article.idArticle == 0L) null else article
    } //&&

    fun changeArticle(articleEntity: ArticleEntity): List<Article>{
        articleEntity.unitId = getAddUnit(articleEntity.unitA as UnitEntity).idUnit
        articleEntity.groupId = getAddGroup(articleEntity.group as GroupEntity).idGroup
        dataDao.changeArticle(articleEntity)
        return getListArticle()
    }

    fun changeGroupArticle( idGroup: Long, articlesId: List<Long>): List<Article>{
        if (idGroup > 0) dataDao.changeGroupArticle(idGroup, articlesId)
        return getListArticle()
    }

    /** Group article*/
    fun getGroups(): List<GroupArticle>{
        return dataDao.getGroups()
    }

    private fun getAddGroup(group: GroupArticle): GroupArticle {
        return if (group.idGroup == 0L) {
            val id = if (group.nameGroup != "") dataDao.addGroup(group as GroupEntity) else 1
            dataDao.getGroup(id)
        } else group
    }

    /** Unit*/
    private fun getAddUnit(unitA: UnitEntity): UnitA {
        return if (unitA.idUnit == 0L) {
            val id = if (unitA.nameUnit != "") dataDao.addUnit(unitA) else 1
            dataDao.getUnit(id)
        } else unitA
    }

    fun getUnits(): List<UnitA>{
        return dataDao.getUnits()
    }

    private fun mapProduct(entity: ProductObj):ProductEntity{

        val product = ProductEntity(
            idProduct = entity.product.idProduct,
            value = entity.product.value,
            position = entity.product.position,
            basketId = entity.product.basketId,
            putInBasket = entity.product.putInBasket,
            articleId = entity.article.article.idArticle,)

        product.article = ArticleEntity(
            idArticle = entity.article.article.idArticle,
            nameArticle = entity.article.article.nameArticle)
        product.article.group = entity.article.group
        product.article.unitA = entity.article.unitA

        return product
    }

    private fun mapArticle(obj: ArticleObj): ArticleEntity{
        val article = ArticleEntity(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle)
        article.group = obj.group
        article.unitA = obj.unitA

        return article
    }
    private fun toUpFirstChar (text: String): String{
        return (text[0].uppercase() + text.substring(1)).trim()
    }
}
