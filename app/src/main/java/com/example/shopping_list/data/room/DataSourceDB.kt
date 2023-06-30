package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.ArticleTable
import com.example.shopping_list.data.room.tables.BasketTable
import com.example.shopping_list.data.room.tables.SectionTable
import com.example.shopping_list.data.room.tables.ProductTable
import com.example.shopping_list.data.room.tables.UnitTable
import com.example.shopping_list.data.room.tables.relation.ArticleObj
import com.example.shopping_list.data.room.tables.relation.ProductObj
import com.example.shopping_list.entity.interfaces.ArticleInterface
import com.example.shopping_list.entity.Article
import com.example.shopping_list.entity.interfaces.BasketInterface
import com.example.shopping_list.entity.interfaces.SectionInterface
import com.example.shopping_list.entity.interfaces.ProductInterface
import com.example.shopping_list.entity.Product
import com.example.shopping_list.entity.Section
import com.example.shopping_list.entity.UnitApp
import com.example.shopping_list.entity.interfaces.UnitInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    /** Manage position product */

    fun buildPositionProduct(basketId: Long){
        val listProduct = dataDao.getListProduct(basketId)
            .map { item -> mapProduct(item) }
            .sortedWith( compareBy ( { it.article.section.idSection }, {it.position}))
        var position = 1
        if (listProduct.isNotEmpty()) {
            listProduct.forEach {
                it.position = position++
                dataDao.setPositionProductInBasket(it.idProduct, basketId, it.position)
            }
        }
    }

    fun setPositionProductInBasket(idProduct: Long, basketId: Long, position: Int){
        dataDao.setPositionProductInBasket(idProduct, basketId, position)
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

    fun changeNameBasket(basket: BasketInterface): List<BasketInterface>{
        dataDao.changeNameBasket(basket.idBasket, basket.nameBasket)
        return getListBasketCount()
    }

    fun getListBasketCount(): List<BasketInterface> {
        val result = dataDao.getListBasket()
        result.forEach { it.quantity = dataDao.countProductInBasket(it.idBasket) }
        return result
    }

    private fun getBasket(basketName: String): Long? {
        return dataDao.checkBasketFromName(basketName)
    }

    fun addBasket(basket: BasketInterface): List<BasketInterface> {
        basket.nameBasket = (basket.nameBasket[0].uppercase()+basket.nameBasket.substring(1)).trim()
        val existBasket = getBasket(basket.nameBasket)
        if (existBasket == null ) {
            dataDao.newBasket(basket as BasketTable)
        }
        return getListBasketCount()
    }

    fun deleteBasket(basketId: Long): List<BasketInterface> {
        dataDao.deleteByIdBasket(basketId)
        dataDao.deleteByIdBasketProduct(basketId)
        return getListBasketCount()
    }

    fun setPositionBasket( direction: Int): List<BasketInterface>{
            reBuildPositionBasket( direction )
        return getListBasketCount()
    }

    fun getNameBasket(basketId: Long): String {
        return dataDao.getNameBasket(basketId)
    }

    /** Product*/

    fun addProduct(product: ProductInterface): List<ProductInterface> {
        if (product.basketId > 0L ) {
            val idArticle = getAddArticle(product.article as ArticleTable)?.idArticle ?: 0
            if (idArticle != 0L) {
                if (dataDao.checkArticleInBasket(product.basketId, idArticle) == null) {
                    dataDao.addProduct(
                        ProductTable(
                            value = product.value,
                            basketId = product.basketId,
                            articleId = idArticle,
                            position = product.position )
                    )
                }  else {
                    throw IllegalArgumentException("error_addProduct")
                }
            }
        }
        return getListProducts(product.basketId)
    }
    fun getListProducts(basketId: Long): List<ProductInterface>{
        return if (basketId > 0) dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
                else emptyList()
    }

    fun putProductInBasket(product: ProductInterface, basketId: Long): List<ProductInterface>{
        dataDao.putProductInBasket(product.idProduct, basketId)
//        buildPositionProduct(basketId)
        return getListProducts(basketId)
//        return reBuildPositionProduct(basketId, 0)
    }

    fun changeProductInBasket(product: ProductInterface, basketId: Long): List<ProductInterface>{
        getAddArticle(product.article )
        if (product.value > 0) dataDao.setValueProduct(
            product.idProduct, basketId,
            product.value
        )
        return dataDao.getListProduct(basketId).map { item -> mapProduct(item) }
    }

    fun deleteSelectedProduct(products: List<ProductInterface>): List<ProductInterface>{
        val basketId = products[0].basketId
        val listId: List<Long> = products.filter { it.isSelected }.map { it.idProduct }
        if (basketId > 0) {
            dataDao.deleteProducts(listId , basketId )
            buildPositionProduct( basketId )
        }
        return getListProducts(basketId)
    }
    /** Article*/
    fun deleteSelectedArticle(articles: List<ArticleInterface>): List<ArticleInterface>{
        articles.forEach {
            if (it.isSelected) {
                if ( dataDao.checkArticleWithProduct(it.idArticle).isEmpty() )
                    dataDao.delArticle(it.idArticle)
            }
        }
        return getListArticle()
    }

    fun sortingArticle(){
        reBuildPositionArticle( 0 ,
            dataDao.getListArticleSortName().map { item -> mapArticle(item) })
    }

    fun getListArticle(): List<ArticleInterface> = dataDao.getListArticle().map { item -> mapArticle(item) }

    fun setPositionArticle( direction: Int): List<ArticleInterface>{
        reBuildPositionArticle( direction , getListArticle())
        return getListArticle()
    }
    private fun reBuildPositionArticle(direction: Int, list: List<ArticleInterface>){
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
    fun getAddArticle(article: ArticleInterface): ArticleInterface? {
        val localArticle = Article(
            unitA = getAddUnit(article.unitA as UnitTable) as UnitApp,
            section = getAddSection(article.section as SectionTable) as Section,
            nameArticle = toUpFirstChar(article.nameArticle),
            idArticle = if (article.idArticle == 0L && article.nameArticle != ""){
                            dataDao.addArticle(article as ArticleTable) }
                        else article.idArticle
        )
        return if (localArticle.idArticle == 0L) null else localArticle
    } //&&

    fun changeArticle(article: ArticleInterface): List<ArticleInterface>{

        val localArticle = ArticleTable(
            unitId = getAddUnit(article.unitA as UnitTable).idUnit,
            sectionId = getAddSection(article.section).idSection,
            nameArticle = toUpFirstChar(article.nameArticle),
            idArticle = article.idArticle,
            position = article.position
        )
        dataDao.changeArticle(localArticle)
        return getListArticle()
    }
    fun changeSectionSelectedProduct(productList: List<ProductInterface>, idSection: Long): List<ProductInterface>{
        val articlesId: List<Long> = productList.filter { it.isSelected }.map { it.article.idArticle }
        dataDao.changeSectionArticle( articlesId, idSection )
        return if (productList[0].basketId > 0) {
            buildPositionProduct(productList[0].basketId)
            getListProducts(productList[0].basketId)
        } else emptyList()
    }
    fun changeSectionArticle(idSection: Long, articlesId: List<Long>): List<ArticleInterface>{
        if (idSection > 0) dataDao.changeSectionArticle( articlesId, idSection )
        return getListArticle()
    }

    /** Section article*/
    fun getSections(): List<SectionInterface>{
        return dataDao.getSections()
    }

    private fun getAddSection(section: SectionInterface): SectionInterface {
        return if (section.idSection == 0L) {
            val id = if (section.nameSection != "") {
//                section.nameSection = toUpFirstChar( section.nameSection )
//                dataDao.addSection(section as SectionEntity)
                dataDao.addSection(SectionTable(
                    nameSection = toUpFirstChar( section.nameSection ),
                    colorSection = section.colorSection
                ))
            } else 1
            dataDao.getSection(id)
        } else section
    }
//    fun sectionFlow(): Flow<List<Section>> = dataDao.getSectionFlow()

    /** Unit*/
    fun getAddUnit(unitA: UnitInterface): UnitInterface {
        val localUnit = unitA as UnitTable
        if (localUnit.idUnit == 0L) {
            localUnit.idUnit = if (localUnit.nameUnit != "") dataDao.addUnit(localUnit) else 1
        }
        else if (localUnit.nameUnit != dataDao.getUnit(localUnit.idUnit).nameUnit)
            dataDao.changeUnit(localUnit)
        return localUnit
    }

    fun deleteUnits(units: List<UnitInterface>) {
        units.forEach { if (dataDao.checkUnit( it.idUnit ) == null) dataDao.deleteUnit(it.idUnit) }
    }

    fun getUnits(): List<UnitInterface>{
        return dataDao.getUnits()
    }

    //    fun unitsFlow(): Flow<List<UnitA>> = dataDao.getUnitsFlow()
    private fun mapProduct(obj: ProductObj): ProductInterface {

        return  Product(
            idProduct = obj.product.idProduct,
            basketId = obj.product.basketId,
            value = obj.product.value,
            putInBasket = obj.product.putInBasket,
            position = obj.product.position,
            article = Article(
                idArticle = obj.article.article.idArticle,
                nameArticle = obj.article.article.nameArticle,
                unitA = UnitApp(
                    idUnit = obj.article.unitA.idUnit,
                    nameUnit = obj.article.unitA.nameUnit) ,
                section = Section(
                    idSection = obj.article.section.idSection,
                    nameSection = obj.article.section.nameSection),
                isSelected = obj.article.article.isSelected,
                position = obj.article.article.position,
            )
        )
    }

    private fun mapArticle(obj: ArticleObj): ArticleInterface {

        return Article(
            idArticle = obj.article.idArticle,
            nameArticle = obj.article.nameArticle,
            unitA = UnitApp(idUnit = obj.unitA.idUnit, nameUnit = obj.unitA.nameUnit),
            section = Section(idSection = obj.section.idSection, nameSection = obj.section.nameSection),
            isSelected = obj.article.isSelected,
            position = obj.article.position,
        )
    }
    private fun toUpFirstChar (text: String): String{
        return (text[0].uppercase() + text.substring(1)).trim()
    }
}
