package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.ArticleEntity
import com.example.shopping_list.data.room.tables.GroupEntity
import com.example.shopping_list.data.room.tables.UnitEntity
import com.example.shopping_list.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    fun getListBasket(): List<Basket>{
        return dataSourceDB.getListBasket()
    }

    fun getListProducts(basketId: Long): List<Product> = dataSourceDB.getListProducts(basketId)

    fun getListArticle(): List<Article> =  dataSourceDB.getListArticle()

    fun getGroups(): List<GroupArticle> = dataSourceDB.getGroups()

    fun getUnits(): List<UnitA> = dataSourceDB.getUnits()

    fun newBasket(basketName: String): List<Basket>{
        return dataSourceDB.newBasket(basketName)
    }
    fun addProduct(product: Product, basketId: Long): List<Product>{
        //Checking unit in db.
        product.basketId = basketId
        if (product.article.unitA!!.idUnit == 0L){
            product.article.unitA!!.idUnit = dataSourceDB.addUnit(product.article.unitA as UnitEntity)
        }
        if (product.article.group!!.idGroup == 0L){
            product.article.group!!.idGroup = dataSourceDB.addGroup(product.article.group as GroupEntity)
        }
        if (product.article.idArticle == 0L){
            product.article.idArticle = dataSourceDB.addArticle(
                ArticleEntity(
                    nameArticle = product.article.nameArticle,
                    groupId = product.article.group!!.idGroup,
                    unitId = product.article.unitA!!.idUnit
                ))
        }

        return dataSourceDB.addProduct(product)
    }
    fun newArticle(name: String): List<Article> {
        return dataSourceDB.newArticleS(name)
    }
//    fun getBasketProducts(basket:BasketDB): List<ProductDB>{
//        var listProduct = emptyList<ProductDB>()
//        dataSourceDB.getBasketProducts(basket).forEach { item->
//            listProduct = item.listProductDB
//        }
//        return listProduct
//    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return dataSourceDB.getGroupsWithProduct()
//    }
}