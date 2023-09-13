package com.example.basket.data.room

import com.example.basket.data.room.tables.ArticleDB
import com.example.basket.data.room.tables.BasketDB
import com.example.basket.data.room.tables.ProductDB
import com.example.basket.data.room.tables.SectionDB
import com.example.basket.data.room.tables.UnitDB
import com.example.basket.data.room.tables.relation.ArticleObj
import com.example.basket.data.room.tables.relation.ProductObj
import com.example.basket.entity.Article
import com.example.basket.entity.Basket
import com.example.basket.entity.Product
import com.example.basket.entity.Section
import com.example.basket.entity.UnitApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    /** Manage position product */

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

    fun addBasket(basket: BasketDB): List<Basket> {
        basket.nameBasket = (basket.nameBasket[0].uppercase()+basket.nameBasket.substring(1)).trim()
        val existBasket = getBasket(basket.nameBasket)
        if (existBasket == null ) {
            dataDao.newBasket(basket as BasketDB)
        }
        return getListBasketCount()
    }

    fun deleteBasket(basketId: Long): List<Basket> {
        dataDao.deleteByIdBasket(basketId)
        dataDao.deleteByIdBasketProduct(basketId)
        return getListBasketCount()
    }

    fun getNameBasket(basketId: Long): String {
        return dataDao.getNameBasket(basketId)
    }

    /** Product*/

    fun addProduct(product: Product, basketId: Long): List<Product> {
        if (basketId > 0L ) {
            val idArticle = getAddArticle(product.article as ArticleDB).idArticle
            if (idArticle != 0L) {
                if (dataDao.checkArticleInBasket( basketId, idArticle) == null) {
                    dataDao.addProduct(
                        ProductDB(
                            value = product.value,
                            basketId = basketId,
                            articleId = idArticle,
                            position = product.position )
                    )
                }  else { throw IllegalArgumentException("error_addProduct") }
            }
        }
        return getListProducts(basketId)
    }
    fun getListProducts(basketId: Long): List<Product>{
        return dataDao.getListProduct(basketId).map {  item -> mapProduct(item) }
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        dataDao.putProductInBasket(product.idProduct, basketId)
        return getListProducts(basketId)
    }

    fun changeProductInBasket(product: Product, basketId: Long): List<Product>{
        getAddArticle(product.article as ArticleDB)
        if (product.value > 0) dataDao.setValueProduct(product.idProduct, basketId, product.value)
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun deleteSelectedProduct(products: List<Product>): List<Product>{
        val basketId = products[0].basketId
        val listId = products.filter { it.isSelected }.map { it.idProduct }
        dataDao.deleteProducts(listId, basketId)
        return getListProducts(basketId)
    }
    /** Article*/
    fun deleteSelectedArticle(articles: List<Article>): List<Article>{
        articles.forEach {
            if (it.isSelected) {
                if ( dataDao.checkArticleWithProduct(it.idArticle).isEmpty() )
                    dataDao.delArticle(it.idArticle)
            }
        }
        return getListArticle()
    }

    fun getListArticle(): List<Article> = dataDao.getListArticle().map { item -> mapArticle(item) }

    fun getAddArticle(article: ArticleDB): ArticleDB {

        if (article.idArticle == 0L || article.sectionId == 0L || article.unitId == 0L) {
            if (article.unitId == 0L) article.unitId = getAddUnit(article.unitApp as UnitDB).idUnit
            if (article.sectionId == 0L) article.sectionId =
                getAddSection(article.section as SectionDB).idSection
            article.nameArticle = toUpFirstChar(article.nameArticle)
            if (article.idArticle == 0L)
                article.idArticle = if (article.nameArticle != "") dataDao.addArticle(article) else 1
            dataDao.updateArticle(article)
        }
        return article
    } //&&f

    fun changeArticle(article: ArticleDB): List<Article>{

        val localArticle = ArticleDB(
            idArticle = article.idArticle,
            nameArticle = toUpFirstChar(article.nameArticle),
            position = article.position,
            sectionId = getAddSection(article.section as SectionDB).idSection,
            unitId = getAddUnit(article.unitApp as UnitDB).idUnit,
        )
        dataDao.changeArticle(localArticle)
        return getListArticle()
    }
    fun changeSectionSelectedProduct(productList: List<Product>, idSection: Long): List<Product>{
        val articlesId = productList.filter { it.isSelected }.map { it.article.idArticle }
        dataDao.changeSectionArticle( articlesId, idSection )
        return if (productList[0].basketId > 0) {
            getListProducts(productList[0].basketId)
        } else emptyList()
    }
    fun changeSectionArticle(idSection: Long, articlesId: List<Long>): List<Article>{
        if (idSection > 0) dataDao.changeSectionArticle( articlesId, idSection )
        return getListArticle()
    }

    /** Section article*/
    fun getSections(): List<Section>{
        return dataDao.getSections()
    }

    private fun getAddSection(section: SectionDB): Section {
        section.nameSection = toUpFirstChar(section.nameSection)
        if (section.idSection == 0L) {
            section.idSection = if (section.nameSection != "") {
                dataDao.getSection(section.nameSection)?.idSection ?: dataDao.addSection(section)
            } else 1
        } else if(section.nameSection != dataDao.getSection(section.idSection).nameSection)
            dataDao.changeSection(section)
        return section
    }

    fun changeSection(section: Section): List<Section> {
        dataDao.changeSection(section as SectionDB)
        return dataDao.getSections()
    }

    fun deleteSections(sections: List<Section>) {
        sections.filter{it.isSelected}.forEach {
            if (dataDao.checkSection( it.idSection ) == null) dataDao.deleteSection(it.idSection) }
    }

    /** Unit*/

    fun getAddUnit(unitA: UnitDB): UnitApp {
        if (unitA.idUnit == 0L) {
            unitA.idUnit = if (unitA.nameUnit != "") {
                dataDao.getUnit(unitA.nameUnit)?.idUnit ?: dataDao.addUnit(unitA)
            } else 1
        } else if (unitA.nameUnit != dataDao.getUnit(unitA.idUnit).nameUnit)
            dataDao.changeUnit(unitA)
        return unitA
    }
    fun deleteUnits(units: List<UnitApp>) {
        units.filter{it.isSelected}.forEach {
            if (dataDao.checkUnit( it.idUnit ) == null) dataDao.deleteUnit(it.idUnit) }
    }

    fun getUnits(): List<UnitApp>{
        return dataDao.getUnits()
    }

    //    fun unitsFlow(): Flow<List<UnitA>> = dataDao.getUnitsFlow()
    private fun mapProduct(obj: ProductObj): Product {

        return ProductDB(
            idProduct = obj.product.idProduct,
            basketId = obj.product.basketId,
            value = obj.product.value,
            putInBasket = obj.product.putInBasket,
            position = obj.product.position,
            articleId = obj.article.article.idArticle,
            article = ArticleDB(
                idArticle = obj.article.article.idArticle,
                nameArticle = obj.article.article.nameArticle,
                position = obj.article.article.position,
                sectionId = obj.article.section.idSection,
                unitId = obj.article.unitA.idUnit,
                isSelected = obj.article.article.isSelected,
                section = obj.article.section,
                unitApp = obj.article.unitA,
            ),
            isSelected = obj.product.isSelected,
        )
    }

    private fun mapArticle(obj: ArticleObj): Article {

        return ArticleDB(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle,
            isSelected = obj.article.isSelected,
            position = obj.article.position,
            sectionId = obj.section.idSection,
            unitId = obj.unitA.idUnit,
            unitApp = obj.unitA,
            section = obj.section,
        )
    }
    private fun toUpFirstChar (text: String): String{
        return (text[0].uppercase() + text.substring(1)).trim()
    }
}
