package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.GroupWithArticle
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    fun getListBasket(): List<Basket>{
        return dataSourceDB.getListBasket()
    }

    fun getListProducts(basketId: Int): List<Product>{
        return dataSourceDB.getListProducts(basketId)
    }
    fun getGroups(): List<String>{
        val list = mutableListOf<String>()
        dataSourceDB.getGroups().forEach {
            list.add(it.nameGroup)
        }
        return list
    }
    fun getUnits(): List<String>{
        val list = mutableListOf<String>()
        dataSourceDB.getUnits().forEach {
            list.add(it.nameUnit)
        }
        return list
    }
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
    fun getGroupsWithProduct(): List<GroupWithArticle>{
        return dataSourceDB.getGroupsWithProduct()
    }
}