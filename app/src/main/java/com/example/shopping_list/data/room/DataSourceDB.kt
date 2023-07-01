package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.BasketEntity
import com.example.shopping_list.data.room.tables.SectionEntity
import com.example.shopping_list.data.room.tables.ProductEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.ArticleClass
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.ProductClass
import com.example.shopping_list.entity.UnitA
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    /** Manage position product */
    private fun movePositionProduct(basketId: Long, direction: Int): List<Product>{
        val listProduct = dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
        val positionEnd = listProduct.size
        var position = 1
        if (listProduct.isNotEmpty()) {
           //listProduct.forEach { if (it.putInBasket) it.position = position++ }
            val positionStart = position
            listProduct.forEach {
//                if (!it.putInBasket) {
                    if (position + direction < positionStart) it.position = positionEnd
                    else if (position + direction > positionEnd) it.position = positionStart
                    else it.position = position + direction
                    position++
//                }
            }
            listProduct.forEach {
                dataDao.setPositionProductInBasket( it.idProduct, basketId, it.position )
            }
        }
        return listProduct.sortedBy { it.position }
    }

    fun buildPositionProduct(basketId: Long){
        val listProduct = dataDao.getListProduct(basketId)
            .map { item -> mapProduct(item) }
            .sortedWith( compareBy ( {it.article.section.idSection}, {it.position}))
        var position = 1
        if (listProduct.isNotEmpty()) {
            listProduct.forEach {
                it.position = position++
                dataDao.setPositionProductInBasket( it.idProduct, basketId, it.position )
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

    fun addProduct(product: Product, basketId: Long): List<Product> {
        if (basketId > 0L ) {
            val idArticle = getAddArticle(product.article as ArticleEntity)?.idArticle ?: 0
            if (idArticle != 0L) {
                if (dataDao.checkArticleInBasket( basketId, idArticle) == null) {
                    dataDao.addProduct(
                        ProductEntity(
                            value = product.value,
                            basketId = basketId,
                            articleId = idArticle,
                            position = product.position )
                    )
                }  else {
                    throw IllegalArgumentException("error_addProduct")
                }
            }
        }
        return getListProducts(basketId)
    }
    fun getListProducts(basketId: Long): List<Product>{
        val  listObj = dataDao.getListProduct(basketId)
        return listObj.map {  item -> mapProduct(item) }
    }

    fun putProductInBasket(product: Product, basketId: Long): List<Product>{
        dataDao.putProductInBasket(product.idProduct, basketId)
        return getListProducts(basketId)
    }

    fun setPositionProductInBasket(basketId: Long, direction: Int): List<Product>{
        return if (basketId > 0) movePositionProduct(basketId, direction)
                else emptyList()
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
        buildPositionProduct(basketId)
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

    fun buildPositionArticle(){
        reBuildPositionArticle( 0 ,
            dataDao.getListArticleSortName().map { item -> mapArticle(item) })
    }

    fun movePositionArticle( direction: Int): List<Article>{
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
        val localArticle = ArticleEntity(
            unitId = getAddUnit(article.unitA as UnitEntity).idUnit,
            sectionId = getAddSection(article.section as SectionEntity).idSection,
            nameArticle = toUpFirstChar(article.nameArticle),
            idArticle = if (article.idArticle == 0L && article.nameArticle != "") dataDao.addArticle(article)
                        else article.idArticle
        )
        return if (localArticle.idArticle == 0L) null else localArticle
    } //&&

    fun changeArticle(article: ArticleEntity): List<Article>{

        val localArticle = ArticleEntity(
            unitId = getAddUnit(article.unitA as UnitEntity).idUnit,
            sectionId = getAddSection(article.section).idSection,
            nameArticle = toUpFirstChar(article.nameArticle),
            idArticle = article.idArticle,
            position = article.position
        )
        dataDao.changeArticle(localArticle)
        return getListArticle()
    }
    fun changeSectionSelectedProduct(productList: List<Product>, idSection: Long): List<Product>{
        val articlesId = productList.filter { it.isSelected }.map { it.article.idArticle }
        dataDao.changeSectionArticle( articlesId, idSection )
        return if (productList[0].basketId > 0) {
            buildPositionProduct(productList[0].basketId)
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

    private fun getAddSection(section: Section): Section {
        return if (section.idSection == 0L) {
            val id = if (section.nameSection != "") {
                dataDao.addSection(SectionEntity( nameSection = toUpFirstChar( section.nameSection ),
                    colorSection = section.colorSection ))
            } else 1
            dataDao.getSection(id)
        } else section
    }

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

    //    fun unitsFlow(): Flow<List<UnitA>> = dataDao.getUnitsFlow()
    private fun mapProduct(obj: ProductObj): Product {

        return ProductClass(
            idProduct = obj.product.idProduct,
            basketId = obj.product.basketId,
            article = ArticleClass(
                idArticle = obj.article.article.idArticle,
                nameArticle = obj.article.article.nameArticle,
                unitA = obj.article.unitA,
                section = obj.article.section,
                isSelected = obj.article.article.isSelected,
                position = obj.article.article.position,
            ),
            value = obj.product.value,
            putInBasket = obj.product.putInBasket,
            isSelected = obj.product.isSelected,
            position = obj.product.position,
        )
    }

    private fun mapArticle(obj: ArticleObj): Article {

        return ArticleClass(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle,
            unitA = obj.unitA,
            section = obj.section,
            isSelected = obj.article.isSelected,
            position = obj.article.position,
        )
    }
    private fun toUpFirstChar (text: String): String{
        return (text[0].uppercase() + text.substring(1)).trim()
    }
}
