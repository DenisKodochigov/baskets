package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.BasketEntity
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.UnitA
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    /** Manage position product*/
    private fun reBuildPositionProduct(basketId: Long, direction: Int){
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

    private fun reBuildPositionBasket(direction: Int){
        val list = getListBasketCount()
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
            list.forEach { dataDao.setPositionBasket(it.idBasket, it.position) }
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

    fun setPositionBasket( direction: Int): List<Basket>{
            reBuildPositionBasket( direction )
        return getListBasketCount()
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
        reBuildPositionProduct(basketId, 0)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun setPositionProductInBasket(basketId: Long, direction: Int): List<Product>{
        return if (basketId > 0) {
            reBuildPositionProduct(basketId, direction)
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
        reBuildPositionProduct(basketId, 0)
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

    fun sortingArticle(){
        reBuildPositionArticle( 0 ,
            dataDao.getListArticleSortName().map { item -> mapArticle(item) })
    }
    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item -> mapArticle(item) }

    fun setPositionArticle( direction: Int): List<Article>{
        reBuildPositionArticle( direction , getListArticle())
        return getListArticle()
    }
    private fun reBuildPositionArticle(direction: Int, list: List<Article>){
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
    fun getAddArticle(article: ArticleEntity): ArticleEntity? {
        article.unitId = getAddUnit(article.unitA as UnitEntity).idUnit
        article.sectionId = getAddSection(article.section as SectionEntity).idSection
        article.nameArticle = toUpFirstChar(article.nameArticle)
        if (article.idArticle == 0L && article.nameArticle != "") {
                article.idArticle = dataDao.addArticle(article)
        }
        return if (article.idArticle == 0L) null else article
    } //&&

    fun changeArticle(articleEntity: ArticleEntity): List<Article>{
        articleEntity.unitId = getAddUnit(articleEntity.unitA as UnitEntity).idUnit
        articleEntity.sectionId = getAddSection(articleEntity.section as SectionEntity).idSection
        dataDao.changeArticle(articleEntity)
        return getListArticle()
    }

    fun changeSectionArticle(idSection: Long, articlesId: List<Long>): List<Article>{
        if (idSection > 0) dataDao.changeSectionArticle(idSection, articlesId)
        return getListArticle()
    }


    /** Section article*/
    fun getSections(): List<Section>{
        return dataDao.getSections()
    }

    private fun getAddSection(section: Section): Section {
        return if (section.idSection == 0L) {
            val id = if (section.nameSection != "") {
                section.nameSection = toUpFirstChar( section.nameSection )
                dataDao.addSection(section as SectionEntity)
            } else 1
            dataDao.getSection(id)
        } else section
    }
    fun sectionFlow(): Flow<List<Section>> = dataDao.getSectionFlow()

    /** Unit*/
    fun getAddUnit(unitA: UnitEntity): UnitA {
        if (unitA.idUnit == 0L) unitA.idUnit = if (unitA.nameUnit != "") dataDao.addUnit(unitA) else 1
        else if (unitA.nameUnit != dataDao.getUnit(unitA.idUnit).nameUnit) dataDao.changeUnit(unitA)
        return unitA
    }

    fun deleteUnits(units: List<UnitA>) {
        units.forEach { if (dataDao.checkUnit( it.idUnit ) == null) dataDao.deleteUnit(it.idUnit) }
    }

    fun getUnits(): List<UnitA>{
        return dataDao.getUnits()
    }
    fun unitsFlow(): Flow<List<UnitA>> = dataDao.getUnitsFlow()
    private fun mapProduct(obj: ProductObj): Product {

        val product = ProductEntity(
            idProduct = obj.product.idProduct,
            value = obj.product.value,
            position = obj.product.position,
            basketId = obj.product.basketId,
            putInBasket = obj.product.putInBasket,
            articleId = obj.article.article.idArticle,)

        product.article = ArticleEntity(
            idArticle = obj.article.article.idArticle,
            nameArticle = obj.article.article.nameArticle)
        product.article.section = obj.article.section
        product.article.unitA = obj.article.unitA

        return product
    }

    private fun mapArticle(obj: ArticleObj): Article {
        val article = ArticleEntity(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle,
            position = obj.article.position,
            unitId = obj.unitA.idUnit,
            sectionId = obj.section.idSection)
        article.section = obj.section
        article.unitA = obj.unitA

        return article
    }
    private fun toUpFirstChar (text: String): String{
        return (text[0].uppercase() + text.substring(1)).trim()
    }
}
