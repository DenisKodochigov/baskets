package com.example.shopping_list.data

import android.util.Log
import com.example.shopping_list.data.room.DataSourceDB
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
    fun newProduct(product: Product): List<Product>{
        return dataSourceDB.newProduct(product)
    }
    fun newArticle(name: String): List<Article> {
        return dataSourceDB.newArticle(name)
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