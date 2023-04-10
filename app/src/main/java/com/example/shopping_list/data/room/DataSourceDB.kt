package com.example.shopping_list.data.room

import com.example.shopping_list.data.room.tables.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataSourceDB  @Inject constructor(private val dataDao:DataDao){

    fun getBaskets(): List<BasketDB>{
        return dataDao.getListBasket()
    }
    fun getGroups(): List<GroupDB>{
        return dataDao.getGroups()
    }
    fun getUnits(): List<UnitDB>{
        return dataDao.getUnits()
    }
    fun getBasket(basketName: String): Long{
        return dataDao.checkBasketFromName(basketName)
    }

    fun addBasket(basketName: String):Long{
        var idBasket = getBasket(basketName)
        if (idBasket == 0L ) {
            idBasket = dataDao.addBasket(BasketDB(basketName = basketName, selected = false))
        }
        return idBasket
    }

    fun getBasketProducts(basket:BasketDB): List<BasketWithProduct>{
//        return emptyList<ProductDB>()
        return dataDao.getBasketWithProducts(basket.idBasket)
    }

    fun addGroup(groupName: String){
        dataDao.addGroup(GroupDB(nameGroup = groupName))
    }

    fun getGroupsWithProduct(): List<GroupWithProducts>{
        return dataDao.getGroupsWithProducts()
    }
}