package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.*
import com.example.shopping_list.entity.Basket
import com.example.shopping_list.entity.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    fun getListBasket(): List<Basket>{
        return dataDao.getListBasket()
    }
    fun getListProducts(basketId: Int): List<Product>{
//        return if (basketId == -1) dataDao.getListProducts()
//                else dataDao.getBasketWithProducts(basketId) as List<Product>
        var listProduct = emptyList<Product>()
        var listProd = emptyList<BasketWithProduct>()
        if (basketId == -1) listProduct = dataDao.getListProducts()
        else listProd = dataDao.getBasketWithProducts(basketId)
        return listProduct
    }
    fun getGroups(): List<GroupDB>{
        return dataDao.getGroups()
    }
    fun getUnits(): List<UnitDB>{
        return dataDao.getUnits()
    }
    fun getBasket(basketName: String): Long? {
        return dataDao.checkBasketFromName(basketName)
    }

    fun addBasket(basketName: String): List<Basket> {
        if (getBasket(basketName) == null ) {
            dataDao.addBasket(BasketDB(nameBasket = basketName))
        }
        return getListBasket()
    }
    fun addProduct(productName: String, basketId: Int): List<Product> {
//        if (getProduct(productName) == null ) {
//            dataDao.addProduct(ProductDB(nameProduct = productName,))
//        }
//        return getListProducts(basketId)
        return emptyList()
    }
    fun getProduct(productName: String): Long? {
        return dataDao.checkProductFromName(productName)
    }
    fun addGroup(groupName: String){
        dataDao.addGroup(GroupDB(nameGroup = groupName))
    }

    fun getGroupsWithProduct(): List<GroupWithArticle>{
        return emptyList() //dataDao.getGroupsWithProducts()
    }
}