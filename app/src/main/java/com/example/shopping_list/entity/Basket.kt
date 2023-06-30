package com.example.shopping_list.entity

import com.example.shopping_list.entity.interfaces.BasketInterface
import com.example.shopping_list.entity.interfaces.ProductInterface

data class Basket(
    override var idBasket: Long = 0,
    override var dateB: Long = 0,
    override var nameBasket: String = "",
    override var fillBasket: Boolean = false,
    override var quantity: Int = 0,
    override var isSelected:Boolean = false,
    override var position: Int = 0,
): BasketInterface{
    fun mapping(item: BasketInterface): Basket{
        return Basket(
            idBasket = item.idBasket,
            dateB = item.dateB,
            nameBasket = item.nameBasket,
            fillBasket = item.fillBasket,
            quantity = item.quantity,
            isSelected = item.isSelected,
            position = item.position,
        )
    }
}

/*
            idBasket = item,
            dateB = item,
            nameBasket = item,
            fillBasket = item,
            quantity = item,
            isSelected = item,
            position = item,
 */