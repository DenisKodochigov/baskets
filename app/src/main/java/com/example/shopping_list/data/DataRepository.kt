package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.entity.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    fun getListBasket(): List<Basket>{
        return dataSourceDB.getListBasket()
    }

    fun getListProducts(basketId: Int): List<Product> = dataSourceDB.getListProducts(basketId)

    fun getListArticle(): List<Article> =  dataSourceDB.getListArticle()

    fun getGroups(): List<GroupArticle> = dataSourceDB.getGroups()

    fun getUnits(): List<UnitA> = dataSourceDB.getUnits()

    fun addBasket(basketName: String): List<Basket>{
        return dataSourceDB.addBasket(basketName)
    }
    fun addProduct(productName: String): List<Product>{
        return dataSourceDB.addProduct(productName, -1)
    }
//    fun getBasketProducts(basket:BasketDB): List<ProductDB>{
//        var listProduct = emptyList<ProductDB>()
//        dataSourceDB.getBasketProducts(basket).forEach { item->
//            listProduct = item.listProductDB
//        }
//        return listProduct
//    }
    fun addGroup(groupName: String){
        dataSourceDB.addGroup(groupName)
    }
//    fun getGroupsWithProduct(): List<GroupWithArticle>{
//        return dataSourceDB.getGroupsWithProduct()
//    }
}