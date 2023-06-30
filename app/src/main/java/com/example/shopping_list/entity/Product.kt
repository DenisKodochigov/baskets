package com.example.shopping_list.entity

import com.example.shopping_list.data.room.tables.ProductTable
import com.example.shopping_list.entity.interfaces.ProductInterface

data class Product(
    override var idProduct: Long = 0,
    override var basketId: Long = 0,
    override var article: Article = Article(),
    override var value: Double = 0.0,
    override var putInBasket: Boolean = false,
    override var isSelected: Boolean = false,
    override var position: Int = 0
): ProductInterface{
    fun mapping(item: ProductInterface): Product{
        return Product(
            idProduct = item.idProduct,
            basketId = item.basketId,
            article = item.article as Article,
            value = item.value,
            putInBasket = item.putInBasket,
            isSelected = item.isSelected,
            position = item.position
        )
    }
}

/*
    idProduct = item,
    basketId = item,
    article = item,
    value = item,
    putInBasket = item,
    isSelected = item,
    position = item

 */