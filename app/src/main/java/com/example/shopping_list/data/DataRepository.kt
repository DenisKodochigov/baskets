package com.example.shopping_list.data

import com.example.shopping_list.data.room.DataSourceDB
import com.example.shopping_list.data.room.tables.BasketDB
import com.example.shopping_list.data.room.tables.GroupWithProducts
import com.example.shopping_list.data.room.tables.ProductDB
import com.example.shopping_list.entity.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val dataSourceDB: DataSourceDB) {

    fun getListBasket(): List<BasketDB>{
        return dataSourceDB.getBaskets()
    }

    fun getProducts(): List<Product>{
        return emptyList<Product>()
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
    fun addBasket(basketName: String): Long{
        return dataSourceDB.addBasket(basketName)
    }
    fun getBasketProducts(basket:BasketDB): List<ProductDB>{
        var listProduct = emptyList<ProductDB>()
        dataSourceDB.getBasketProducts(basket).forEach { item->
            listProduct = item.listProductDB
        }
        return listProduct
    }
    fun addGroup(groupName: String){
        dataSourceDB.addGroup(groupName)
    }
    fun getGroupsWithProduct(): List<GroupWithProducts>{
        return dataSourceDB.getGroupsWithProduct()
    }
}